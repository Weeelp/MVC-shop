PORT=8080

.PHONY: run stop build javadoc

run: stop
	mvn jetty:run

stop:
	@PID=$$(ss -lntp "sport = :$(PORT)" | grep -oP 'pid=\K\d+'); \
	if [ ! -z "$$PID" ]; then \
		kill -9 $$PID; \
		sleep 1; \
	fi

build:
	mvn clean package

javadoc:
	mvn javadoc:javadoc
