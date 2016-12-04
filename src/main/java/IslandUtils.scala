import scala.collection.mutable.{Set => MSet}
import scala.language.implicitConversions

object IslandUtils {

  class NamingUtils[E](private val data: Seq[E]) extends AnyVal {

    def getCrossoverSeq(combiner: (E, E) => Option[E]): Seq[E] = {
      val groupedAgents = data.grouped(2)
      groupedAgents.flatMap {
        case Seq(a, b) =>
          combiner(a, b)
        case Seq(a) =>
          Iterator.single(a)
      }.toSeq
    }

  }

  implicit def setNamingUtils[Agent](set: MSet[Agent]): NamingUtils[Agent] =
    new NamingUtils(set.toSeq)

}
