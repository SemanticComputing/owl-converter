package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.ws._
//import scala.concurrent.Future
import play.api.Play.current
import utils.OWLAPIWrapper
import org.semanticweb.owlapi.formats.TurtleDocumentFormat
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat
import org.semanticweb.owlapi.formats.ManchesterSyntaxDocumentFormat
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat
import org.semanticweb.owlapi.formats.LatexDocumentFormat
import org.semanticweb.owlapi.formats.KRSS2DocumentFormat
import java.net.URI

class Application extends Controller {

  // Supported formats: Turtle, RDF/XML, OWL Functional Syntax, Manchester OWL Syntax, OWL/XML, Latex, KRSS2
  
  val form = Form(
    tuple(
      "onto" -> optional(text),
      "to" -> optional(text),
      "force-accept" -> optional(text)
    )
  )
  
  def index() = Action { implicit request =>
   
    //TODO: handle too long GET requests (show error instead of white)
    
    form.bindFromRequest().fold(
      formWithErrors => {
        // binding failure, you retrieve the form containing errors
        BadRequest("Bad request.")
      },
      form => {
        // binding success, you get the actual value
        val onto = form._1
        val to = form._2
        val forceAccept = form._3
            
        if (onto == None)
          Ok(views.html.index())
        else {
          val toFormat = to match {
            case Some(s) => s
            case None => "ttl"
          }

          val docFormat = toFormat match {
            case "ttl" => new TurtleDocumentFormat
            case "rdfxml" => new RDFXMLDocumentFormat 
            case "func" => new FunctionalSyntaxDocumentFormat
            case "manc" => new ManchesterSyntaxDocumentFormat
            case "owlxml" => new OWLXMLDocumentFormat
            case "latex" => new LatexDocumentFormat
            case "krss2" => new KRSS2DocumentFormat
            // TODO: handle invalid format: display error message
          }

          if (forceAccept != None && forceAccept != Some("text/plain"))
              BadRequest("Invalid force-accept value.")
          else {
            val mimetype = if (forceAccept != None)
              "text/plain"
            else 
              toFormat match {
                case "ttl" => "text/turtle"
                case "rdfxml" => "application/rdf+xml" 
                case "func" => "text/owl-functional"
                case "manc" => "text/owl-manchester"
                case "owlxml" => "application/owl+xml"
                case "latex" => "application/x-tex"
                case "krss2" => "text/krss2"
              }
            
              // TODO: if ontology == URI, fetch it
            //val uriPattern = """http""".r
            /*try {
              val uri = new URI(onto.get)
              //if (uri.getScheme == "http" || uri.getScheme == "https")
              if (uri.getScheme == "http") {
                println("found an HTTP URI: "+ onto.get)
                //val holder = WS.url(onto.get)
                implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext
                println("jouni")
                val resp = WS.url(onto.get).get.map { response => response }
                resp onSuccess {
                  case response => println(response)
                }
                //resp.future
                println(resp)
                //resp.getresponse
                //println(resp.complete)
                //println(WS.url(onto.get).get.value)
                println("jouni2")
                
              }
            }
            catch {
              case e: Exception =>  
              
            }*/

//            val holder : WSRequestHolder = WS.url(url)
            
              val output = OWLAPIWrapper.convert(onto.get, docFormat)
              // TODO: error handling
  
              Ok(output).as(mimetype)
          }
        }
      }
    )
  }
}
