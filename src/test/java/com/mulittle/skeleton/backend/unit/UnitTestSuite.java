package com.mulittle.skeleton.backend.unit;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({ "com.mulittle.skeleton.backend.unit.parser", "com.mulittle.skeleton.backend.unit.context" })
public class UnitTestSuite {
}
