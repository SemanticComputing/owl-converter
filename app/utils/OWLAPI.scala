package utils

import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.io.StringDocumentSource
import org.semanticweb.owlapi.io.StringDocumentTarget
import org.semanticweb.owlapi.model.OWLDocumentFormat

object OWLAPIWrapper {
  
  def convert(data: String, toFormat: OWLDocumentFormat) : String = {
    val manager = OWLManager createOWLOntologyManager
    val onto = manager.loadOntologyFromOntologyDocument(new StringDocumentSource(data))
    val target = new StringDocumentTarget
    onto.saveOntology(toFormat, target)
    return target toString
  }
}