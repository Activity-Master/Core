

gcloud auth application-default login

gcloud config configurations create w1-wpcw-product-incidents
gcloud config configurations activate  w1-wpcw-product-incidents
gcloud config set project  ecp-wtr-product-incident-stub
gcloud auth application-default set-quota-project  ecp-wtr-product-incident-stub
gcloud auth configure-docker europe-west2-docker.pkg.dev
gcloud auth login
gcloud auth application-default login

