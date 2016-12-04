package pl.edu.agh.to.util

import java.util.Optional

import scala.language.implicitConversions

object OptionalUtils {

  implicit def asScala[E](opt: Optional[E]): Option[E] = {
    if (opt.isPresent) {
      Some(opt.get())
    } else {
      None
    }
  }

  implicit def asJava[E](opt: Option[E]): Optional[E] = {
    opt match {
      case Some(value) =>
        Optional.of(value)
      case None =>
        Optional.empty()
    }
  }

}
