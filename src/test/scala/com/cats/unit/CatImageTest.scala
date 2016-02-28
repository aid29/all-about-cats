package com.cats.unit

import com.cats.CatImage
import org.scalatest.FunSpec
import org.scalatest.Matchers._

class CatImageTest extends FunSpec{

  describe("Cat Image") {
    it("build from xml") {
      val xmlRes = <response>
        <data>
          <images>
            <image>
              <url>http://24.media.tumblr.com/tumblr_m4j2e7Fd7M1qejbiro1_1280.jpg</url>
              <id>dqd</id>
              <source_url>http://thecatapi.com/?id=dqd</source_url>
            </image>
          </images>
        </data>
      </response>
      CatImage(xmlRes) should be(CatImage("http://24.media.tumblr.com/tumblr_m4j2e7Fd7M1qejbiro1_1280.jpg","dqd"))
    }
  }

}
