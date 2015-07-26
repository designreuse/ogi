FROM jerep6/ogi-base
MAINTAINER jerep6

USER ogi
# Load configuration for cron for ogi user
ADD cron.conf /tmp/
RUN crontab /tmp/cron.conf

USER root

