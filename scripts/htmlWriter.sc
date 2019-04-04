
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

/* Project-specific CEX Stuff */

val myCexFile:String = "john.cex"

lazy val lib = loadLibrary(myCexFile)
lazy val tr = lib.textRepository.get
lazy val johnCorpus = tr.corpus

// Avoid typing lengthy URNs all the time
def u(passage:String):CtsUrn = {
	val baseUrl:String = "urn:cts:greekLit:tlg0031.tlg004.reina:"
	CtsUrn(s"${baseUrl}${passage}")
}

// Quick access to the ID of a poetic book
def whichBook(u:CtsUrn):String = {
	if (u.passageComponent.size > 0) {
		u.collapsePassageTo(1).passageComponent
	} else {
		"1–21"
	}
}

// Getting labels for a URN
//     note that these depend on the stuff defined above
val groupName:String = tr.catalog.groupName(u(""))
val workTitle:String = tr.catalog.workTitle(u(""))
val versionLabel:String = tr.catalog.versionLabel(u(""))


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



/* HTML stuff */

var htmlTop:String = s"""<!DOCTYPE html>
<html>
<head>
	<title>${groupName}: ${workTitle}</title>
	<link href="https://fonts.googleapis.com/css?family=Arbutus+Slab|Eczar" rel="stylesheet">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<link rel="stylesheet" type="text/css" href="style.css">
	<style>
		STYLES_GO_HERE
	</style>
</head>

<body>
"""

var htmlBottom:String = """</body></html>"""

/* Now we build the website */

val bookChunks:Vector[Corpus] = chunkByCitation(johnCorpus, 1)


def buildSite:Unit = {
	for ( bk <- bookChunks.zipWithIndex) {

		// grab the chapter's id (in this case, the Book's number)
		val bkNum:Int = bk._2 + 1
		// grab the Corpus so it is easy to use
		val c:Corpus = bk._1

		// create a unique filename for each book
		val htmlName:String = s"book${bkNum}.html"

		/* Navigation */
		val prevLink:String = {
			bkNum match {
				case n if (n == 1) => { "" }
				case _ => { s"""<a href="book${bkNum - 1}.html">previous</a>""" }
			}
		}
		val nextLink:String = {
			bkNum match {
				case n if (n == (bookChunks.size)) => { "" }
				case _ => { s"""<a href="book${bkNum + 1}.html">next</a>""" }
			}
		}
		val nav:String = s"""<div class="nav">${prevLink} | ${nextLink}</div>"""
		/* End Navigation */

		/* Chapter Heading */
		val bookHeader:String = s"""
			<div style = "text-align: center; color: maroon; font-size: 2.5em"; class="bookHeader color1">
				<p class="textOnColor">Capitulo ${bkNum}</p>
			</div>
		"""


		// create a container with all the CitableNodes for this chunk
		val containerOpen:String = s"""<div class="text Capitulo${bkNum}">"""
		val containerClose:String = """</div>"""

		val passages:Vector[String] = c.nodes.map( n => {
			s"""<p><span class="cite">${n.urn.passageComponent}</span>${n.text}</p>"""
		})

		// save this chunk as an html file
		val htmlString:String = {
			htmlTop +
			nav +
			bookHeader +
			containerOpen +
			passages.mkString("\n") +
			containerClose +
			nav +
			htmlBottom
		}
		// Write out to a file
		saveString(htmlString, "html/", htmlName)
	}
}

println(s"\n-------------\nUse 'buildSite' to make HTML pages\n-------------\n")
