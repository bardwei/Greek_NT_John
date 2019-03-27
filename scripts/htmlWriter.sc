
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

def loadFile(fp:String = "../iliad_alignment/iliad_pope.txt"):Vector[String] = {
	Source.fromFile(fp).getLines.toVector
}

def saveString(s:String, filePath:String = "texts/", fileName:String = "temp.txt"):Unit = {
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

// Here's how you can easily make a new URN:

val oneVerseInThreeVersions:CtsUrn = CtsUrn(johnStr + "1.1")
val oneVerseInOneVersion:CtsUrn = CtsUrn(englishStr + "1.1")
val threeVersesInOneVersion:CtsUrn = CtsUrn(spanishStr + "1.1-1.2")

// Here's how you can grab a chunk of your library

val newCorpus:Corpus = tr.corpus ~~ oneVerseInThreeVersions
// see it with…
printCorpus(newCorpus)

// Getting labels for a URN
tr.catalog.groupName(oneVerseInThreeVersions)
tr.catalog.workTitle(oneVerseInThreeVersions)
tr.catalog.versionLabel(oneVerseInThreeVersions)

// Getting parts of the URN
threeVersesInOneVersion.passageComponent
