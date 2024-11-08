{{- define "db.fullname" -}}
{{- printf "%s-%s" .Release.Name "db" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "db.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}
