package pl.edu.agh.to.core.util

import scala.collection.concurrent.TrieMap
import scala.collection.generic.{CanBuildFrom, MutableSetFactory}
import scala.collection.mutable.{SetLike, Set => MSet}

class TrieSet[T] extends MSet[T] with SetLike[T, TrieSet[T]] {
  private[this] val content: TrieMap[T, Unit] = TrieMap.empty

  override def +=(elem: T): TrieSet.this.type = {
    content += elem -> ()
    this
  }

  override def -=(elem: T): TrieSet.this.type = {
    content -= elem
    this
  }


  override def empty: TrieSet[T] = TrieSet.empty

  override def contains(elem: T): Boolean = content.contains(elem)

  override def iterator: Iterator[T] = content.keysIterator

  override def toString(): String = content.keySet.toString()
}

object TrieSet extends MutableSetFactory[TrieSet] {
  implicit def canBuildFrom[A]: CanBuildFrom[Coll, A, TrieSet[A]] = setCanBuildFrom

  override def empty[A]: TrieSet[A] = new TrieSet
}
