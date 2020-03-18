# OWL Syntax Converter

OWL syntax converter is a web service for converting OWL ontologies from one syntax to another.

The service is a simple HTTP wrapper based on [OWL API](https://github.com/owlcs/owlapi/), and is implemented using [Play Framework](https://www.playframework.com/).

Supported OWL syntax formats: Turtle, RDF/XML, OWL Functional Syntax, Manchester OWL Syntax, OWL/XML, Latex, and KRSS2.

GET/POST parameters:

`onto`: OWL ontology content

`to`: output serialization format (ttl, rdfxml, func, manc, owlxml, latex, krss2), default: ttl


## Running in Docker

Run (pulls first the image from Docker Hub): `docker run -p 9000:9000 -it --rm --name owl-converter secoresearch/owl-converter`

Or build yourself and run:

Build: `docker build -t owl-converter .`

Run: ` docker run -p 9000:9000 -it --rm --name owl-converter owl-converter`

Usage:
Make a HTTP request to URL:
`http://localhost:9000/?onto=ONTOLOGY_CONTENT&to=FORMAT`

E.g.
`wget -O - "http://localhost:9000/?onto=<http://example.com/s>+<a>+<http://example.com/o>+.&to=func"`

When accessed without parameters, the URL provides a simple HTML form for using the service and documentation, as well.
