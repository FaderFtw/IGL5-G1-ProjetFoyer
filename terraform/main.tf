provider "aws" {
  region = var.aws_region
}


resource "aws_vpc" "my_vpc" {
  cidr_block = var.vpc_cidr
}

# Create two subnets within the new VPC
resource "aws_subnet" "subnet1" {
  vpc_id                  = aws_vpc.my_vpc.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "us-east-1a"
  map_public_ip_on_launch = true
}

resource "aws_subnet" "subnet2" {
  vpc_id                  = aws_vpc.my_vpc.id
  cidr_block              = "10.0.2.0/24"
  availability_zone       = "us-east-1b"
  map_public_ip_on_launch = true
}


resource "aws_eks_cluster" "my_cluster" {
  name     = var.cluster_name
  role_arn = var.role_arn
  version  = "1.30"

  vpc_config {
    subnet_ids = [aws_subnet.subnet1.id, aws_subnet.subnet2.id]
  }

  depends_on = [
    aws_vpc.my_vpc,
    aws_subnet.subnet1,
    aws_subnet.subnet2
  ]
}

data "aws_eks_cluster" "existing" {
  name = aws_eks_cluster.my_cluster.name
}

resource "aws_security_group_rule" "worker_port_8083" {
  type              = "ingress"
  from_port         = 8080
  to_port           = 8080
  protocol          = "tcp"
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = data.aws_eks_cluster.existing.vpc_config[0].cluster_security_group_id
}

resource "aws_security_group_rule" "worker_port_30000" {
  type              = "ingress"
  from_port         = 30000
  to_port           = 30000
  protocol          = "tcp"
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = data.aws_eks_cluster.existing.vpc_config[0].cluster_security_group_id
}


resource "aws_eks_node_group" "my_node_group" {
  cluster_name    = aws_eks_cluster.my_cluster.name
  node_group_name = "NodeGroup"
  node_role_arn   = var.role_arn
  subnet_ids      = [aws_subnet.subnet1.id, aws_subnet.subnet2.id]

  scaling_config {
    desired_size = 2
    max_size     = 3
    min_size     = 1
  }

  depends_on = [
    aws_eks_cluster.my_cluster
  ]
}
