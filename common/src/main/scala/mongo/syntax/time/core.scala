package mongo.syntax.time

object core extends AnySyntax with DateSyntax {
  type ->[+A, +B] = (A, B)
}
