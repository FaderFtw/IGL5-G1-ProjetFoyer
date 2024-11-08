{{- define "reverse.fullname" -}}
{{- printf "%s-%s" .Release.Name "nginx" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "reverse.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}
