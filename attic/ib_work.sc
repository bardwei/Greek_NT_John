import scala.io.Source
import edu.holycross.shot.cite



val filepath:String = "/vagrant/NT_John/john.cex"
val myBook:Vector[String] = Source.fromFile(filepath).getLines.toVector.filter(_.size > 0)



/* Testing John */
val expectedChapters:Int = 21
val expectedChapterMap:Map(Int, Int) = Map(
  1 -> 51,
  2 -> 25
)
/* End test data */
