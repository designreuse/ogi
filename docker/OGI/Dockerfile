FROM jerep6/ogi-base
MAINTAINER jerep6

# Load configuration for cron
ADD cron.conf /tmp/
RUN crontab /tmp/cron.conf && rm /tmp/cron.conf

