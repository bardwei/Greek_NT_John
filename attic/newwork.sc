import scala.io.Source
import edu.holycross.shot.cite._



val filepath:String = "/vagrant/NT_John/NT_RVR_1858.txt"
lazy val myBook:Vector[String] = Source.fromFile(filepath).getLines.toVector.filter(_.size > 0)

val urn1:CtsUrn = CtsUrn("urn:cts:namespace:group.work.vers:1.1")


CAPITULO [0-9]+

\n[a-zA-Z]

zipWithIndex is gonna be my friend

\n[^0-9\n]

replace with " "$1
