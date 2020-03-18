FROM openjdk:8-jdk-alpine

ENV SBT_VERSION 1.3.4
ENV SBT_HOME /usr/local/sbt

# Install bash, wget and unzip
RUN apk update && \
    apk --no-cache --update add bash wget unzip && \
	# Install sbt
    mkdir -p "$SBT_HOME" && \
    wget -q "https://github.com/sbt/sbt/releases/download/v$SBT_VERSION/sbt-$SBT_VERSION.tgz" && \
    tar -C $SBT_HOME --strip-components=1 -xzf sbt-$SBT_VERSION.tgz && \
    $SBT_HOME/bin/sbt sbtVersion && \
	rm sbt-$SBT_VERSION.tgz

# Environment
ENV PATH_OWLCONVERTER_SRC "/owl-converter-src"
ENV PATH_OWLCONVERTER "/owl-converter"

# Copy and compile owl-converter
WORKDIR "$PATH_OWLCONVERTER_SRC"
COPY build.sbt ./
COPY app ./app
COPY conf ./conf
COPY project ./project
COPY public ./public
RUN $SBT_HOME/bin/sbt dist
RUN unzip -d "/tmp/" "$PATH_OWLCONVERTER_SRC/target/universal/owlconverter-1.0-SNAPSHOT.zip"
RUN mv "/tmp/owlconverter-1.0-SNAPSHOT" "$PATH_OWLCONVERTER"

# Permissions
RUN chmod g=u "$PATH_OWLCONVERTER"

WORKDIR "$PATH_OWLCONVERTER"
RUN rm -rf "$PATH_OWLCONVERTER_SRC"
USER 8009

ENTRYPOINT [ "/owl-converter/bin/owlconverter" ]