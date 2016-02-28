package com.cats.unit

import com.cats.CatCategory
import org.scalatest.FunSpec
import org.scalatest.Matchers._

class CatCategoryTest extends FunSpec {

  it("parse xml response to a list of CatCategory") {
    val categoriesXml = <response>
      <data>
        <categories>
          <category>
            <id>1</id>
            <name>hats</name>
          </category>
          <category>
            <id>2</id>
            <name>space</name>
          </category>
          <category>
            <id>6</id>
            <name>caturday</name>
          </category>
          </categories>
        </data>
      </response>
    val categories: List[CatCategory] = CatCategory.fromXml(categoriesXml)
    categories(0) should be(CatCategory(6,"caturday"))
    categories(1) should be(CatCategory(1,"hats"))
    categories(2) should be(CatCategory(2,"space"))
  }
}
