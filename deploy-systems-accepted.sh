#!/bin/sh
mvn deploy -Dproject.repository.url=https://vsdenexus.esoc.esa.int/nexus/content/repositories/systems-accepted -DskipTests -Dskip.p2=false
