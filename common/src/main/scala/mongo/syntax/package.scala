package mongo

package object syntax {
  object all
      extends RefinedSyntax
         with GenericSyntax
         with CirceSyntax
         with JavaTimeSyntax
         with ConfigDecoderSyntax
         with ListSyntax

  object generic extends GenericSyntax
  object circe extends CirceSyntax
  object refined extends RefinedSyntax
  object javaTime extends JavaTimeSyntax
  object config extends ConfigDecoderSyntax
  object list extends ListSyntax
}
