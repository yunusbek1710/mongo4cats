package mongo.syntax.time

object any extends AnySyntax

trait AnySyntax {
  implicit def AnySyntaxExtension[A](a: A): AnySyntaxExtension[A] = new AnySyntaxExtension[A](a)
}

final class AnySyntaxExtension[A](private val a: A) extends AnyVal {
  def const: Any => A = _ => a

  def pieceOf(seq: Seq[A]): Boolean = seq.contains(a)
}
