
import scala.io.Source
import java.io._
import scala.collection.mutable.LinkedHashMap
import edu.holycross.shot.scm._
import edu.holycross.shot.cite._
import edu.holycross.shot.citeobj._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.seqcomp._
import edu.furman.classics.citealign._

val demoLib:String = "john.cex"

def loadLibrary(fp:String = demoLib):CiteLibrary = {
	val library = CiteLibrary(Source.fromFile(fp).getLines.mkString("\n"),"#",",")
	library
}


def saveString(s:String, filePath:String = "html/", fileName:String = "john.cex"):Unit = {
	val pw = new PrintWriter(new File(filePath + fileName))
	for (line <- s.lines){
		pw.append(line)
		pw.append("\n")
	}
	pw.close
}



def printCorpus(c:Corpus):Unit = {
	println("------")
	for (n <- c.nodes) {
		// Use either this line:
		val thisCitation:String = n.urn.toString
		// or this line:
		//val thisCitation:String = n.urn.passageComponent.toString
		val thisText:String = n.text
		println(s"${thisCitation} :: ${thisText}")
	}
	println("------")
}

lazy val lib = loadLibrary()
lazy val tr = lib.textRepository.get
lazy val cr = lib.collectionRepository.get
lazy val rs = lib.relationSet.get


val johnStr:String = "urn:cts:greekLit:tlg0031.tlg004:"
val englishStr:String = "urn:cts:greekLit:tlg0031.tlg004.kjv_fu:"
val spanishStr:String = "urn:cts:greekLit:tlg0031.tlg004.reina:"
val greekStr:String = "urn:cts:greekLit:tlg0031.tlg004.wh_fu:"




// Getting labels for a URN
/*tr.catalog.groupName(oneVerseInThreeVersions)
tr.catalog.workTitle(oneVerseInThreeVersions)
tr.catalog.versionLabel(oneVerseInThreeVersions)*/

// Getting parts of the URN
threeVersesInOneVersion.passageComponent

lazy val lib = loadLibrary()
lazy val tr = lib.textRepository.get
lazy val johnCorpus = tr.corpus ~~ CtsUrn(spanishStr)

// I'm lazy
def u(passage:String):CtsUrn = {
	val baseUrl:String = "urn:cts:greekLit:tlg0031.tlg004.reina:"
	CtsUrn(s"${baseUrl}${passage}")
}

def whichBook(u:CtsUrn):String = {
	if (u.passageComponent.size > 0) {
		u.collapsePassageTo(1).passageComponent
	} else {
		"1–21"
	}
}

// Chunk-by-citation
def chunkByCitation(c:Corpus, level:Int = 1):Vector[Corpus] = {
	// We need this, for this process only…
	import scala.collection.mutable.LinkedHashMap
	// we start with a Vector of CitableNodes from our corpus
	val v1:Vector[CitableNode] = c.nodes
	// We zipWithIndex to capture their sequence
	val v2:Vector[(CitableNode, Int)] = v1.zipWithIndex
	val v3:Vector[(CtsUrn, Vector[(CitableNode, Int)])] = {
		v2.groupBy( _._1.urn.collapsePassageTo(level) ).toVector
	}
	val v4 = LinkedHashMap(v3.sortBy(_._2.head._2): _*)
	val v5 = v4.mapValues(_ map (_._1)).toVector
	val corpusVec:Vector[Corpus] = v5.map( v => {
		val nodes:Vector[CitableNode] = v._2
		Corpus(nodes)
	})
	corpusVec
}



def htmlTop:String = """<html><body>"""
def htmlBottom:String = """</body></html>"""

val bookChunks:Vector[Corpus] = chunkByCitation(johnCorpus, 1)

for ( bk <- bookChunks.zipWithIndex) {
	val bkNum:Int = bk._2 + 1
	val c:Corpus = bk._1
	val htmlName:String = s"book${bkNum}.html"
	val textString:String = c.nodes.mkString("\n")
	saveString(textString, "html/", htmlName)
}
