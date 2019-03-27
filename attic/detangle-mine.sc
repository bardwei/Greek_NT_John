import scala.io.Source
import edu.holycross.shot.cite._
import java.io._

case class IndexedLine(text:String, index:Int)
case class ChapterHeading(title:String, index:Int)
case class BookPara(chapterName:String, text:String, index:Int)

val filePath:String = "../NT_John/"
def saveString(s:String, filePath:String = filePath, fileName:String = "temp.txt"):Unit = {
  val pw = new PrintWriter(new File(filePath + fileName))
  for (line <- s.lines){
    pw.append(line)
    pw.append("\n")
  }
  pw.close
}


val filepath:String = "../NT_John/NT_RVR_1858.txt"
val myLines:Vector[String] = Source.fromFile(filepath).getLines.toVector.filter( _.size > 0 )

// Grab line numbers
val indexedFileLines:Vector[IndexedLine] = myLines.zipWithIndex.map( ln => {
  new IndexedLine(ln._1, ln._2)
})
// Filter out chapter headings

val chapters:Vector[ChapterHeading] = {
  indexedFileLines.filter(_.text.startsWith("CAPITULO")).map(c => {
    val index:Int = c.index
    val newTitle:String = c.text.replaceAll("CAPITULO *","")
    new ChapterHeading (newTitle, index)
  })
}

val realParagraphs:Vector[IndexedLine] = {
  indexedFileLines.filter(_.text.startsWith("CAPITULO") == false )
}
// find where each chapter begins and ends!
val chapterRanges:Vector[Vector[ChapterHeading]] = chapters.sliding(2,1).toVector

val allButTheLastChapter:Vector[BookPara] = chapterRanges.map( cr => {
  val thisChapt:ChapterHeading = cr.head
  val thisChaptLineNum:Int = thisChapt.index

  val nextChapt:ChapterHeading = cr.last
  val nextChaptLineNum:Int = nextChapt.index

  val chapterParas:Vector[IndexedLine] = {
    realParagraphs.filter( il => {
      (( il.index > thisChaptLineNum ) & (il.index < nextChaptLineNum))
    })
  }
  val bookParas:Vector[BookPara] = chapterParas.map( cp => {
    new BookPara( thisChapt.title, cp.text, cp.index)
  })
  bookParas
}).flatten

val theLastChapter:Vector[BookPara] = {

  val lastChaptHeading:String = chapterRanges.last.last.title
  val lastChaptLineNum:Int = chapterRanges.last.last.index
  val chapterParas:Vector[IndexedLine] = {
    realParagraphs.filter( il => {
      (il.index > lastChaptLineNum)
    })
  }
 val bookParas:Vector[BookPara] = chapterParas.map( cp => {
   new BookPara( lastChaptHeading, cp.text, cp.index)
 })
 bookParas
}

val allChapterLines:Vector[BookPara] = {
  allButTheLastChapter ++ theLastChapter
}

val savableLines:Vector[String] = {
  allChapterLines.map( cl => {
    val cexUrnStr:String = s"urn:cts:greekLit:tlg0031.tlg004.reina:${cl.chapterName.trim}.${cl.text.replaceAll("^([0-9]+).+", "$1")}"
    println(cexUrnStr)
    val cexUrn:CtsUrn = CtsUrn(cexUrnStr)
    s"${cexUrn}#${cl.text.replaceAll("^[0-9]+ ","")}"
  })
}

val cexData:String = savableLines.mkString("\n")
