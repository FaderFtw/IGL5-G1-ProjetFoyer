{{- define "backend.fullname" -}}
{{- printf "%s-%s" .Release.Name "backend" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "backend.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}
