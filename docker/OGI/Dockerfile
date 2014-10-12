FROM jerep6/ogi-base
MAINTAINER jerep6

# Add delivery envelope
ADD OGI-0.4.5 /app/ogi/delivery/OGI-0.4.5
RUN /app/bin/ogi_deploy --deploy-ws --deploy-ihm --deploy-resources --app-version 0.4.5
