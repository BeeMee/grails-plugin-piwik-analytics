package piwik

import grails.test.mixin.TestFor
import grails.util.Environment

@TestFor(PiwikTagLib)
class PiwikTagLibTests {

	static piwikurl = "http://example.com/piwik"

	static siteid = 1

	static expected = """
<!-- Piwik -->
<script type="text/javascript">
var _paq = _paq || [];
_paq.push([ "trackPageView" ]);
_paq.push([ "enableLinkTracking" ]);
(function() {
  var u = (("https:" == document.location.protocol) ? "https" : "http")
  + "://${piwikurl}";
  _paq.push([ "setTrackerUrl", u + "piwik.php" ]);
  _paq.push([ "setSiteId", "${siteid}" ]);
  var d = document, g = d.createElement("script"), s = d.getElementsByTagName("script")[0];
  g.type = "text/javascript";
  g.defer = true;
  g.async = true;
  g.src = u + "piwik.js";
  s.parentNode.insertBefore(g, s);
})();
</script>
<!-- End Piwik Code -->"""

	def tagLib

	@Before
	void setUp(){
		tagLib = applicationContext.getBean(PiwikTagLib)
		tagLib.grailsApplication = [ config: [:] ]
	}

	// *****************************************************
	// * TESTS
	// *****************************************************

	void testTrackPageviewDefaultDisabledInDevelopment() {
		setEnvironment(Environment.DEVELOPMENT)
		setConfigVariables()

		assert tagLib.trackPageview() == ""
	}

	void testTrackPageviewExplicitlyEnabledInDevelopment() {
		setEnvironment(Environment.DEVELOPMENT)
		setConfigVariables([enabled : true])

		assert tagLib.trackPageview().toString() == expected
	}

	void testTrackPageviewDefaultDisabledInTest() {
		setEnvironment(Environment.TEST)
		setConfigVariables()

		assert tagLib.trackPageview() == ""
	}

	void testTrackPageviewExplicitlyEnabledInTest() {
		setEnvironment(Environment.TEST)
		setConfigVariables([enabled : true])

		assert tagLib.trackPageview().toString() == expected
	}

	void testTrackPageviewDefaultEnabledInProduction() {
		setEnvironment(Environment.PRODUCTION)
		setConfigVariables()

		assert tagLib.trackPageview().toString() == expected
	}

	void testTrackPageviewExplicitlyDisabledInProduction() {
		setEnvironment(Environment.PRODUCTION)
		setConfigVariables([enabled : false])

		assert tagLib.trackPageview() == ""
	}

	void testTrackPageviewEnabled() {
		setConfigVariables([enabled : true])

		assert tagLib.trackPageview().toString() == expected
	}

	void testTrackPageviewDisabled() {
		setConfigVariables([enabled : false])

		assert tagLib.trackPageview() == ""
	}

	void testTrackPageviewNoUrlButExplicitlyEnabled() {
		setConfigVariables([enabled : true, url: null])

		assert tagLib.trackPageview() == ""
	}

	void testTrackPageviewCustomTrackingCodeAsStringAttr() {
		setConfigVariables([enabled : true])

		def expected = """
<!-- Piwik -->
<script type="text/javascript">
var _paq = _paq || [];
customTrackingCode();
_paq.push([ "trackPageView" ]);
_paq.push([ "enableLinkTracking" ]);
(function() {
  var u = (("https:" == document.location.protocol) ? "https" : "http")
  + "://${piwikurl}";
  _paq.push([ "setTrackerUrl", u + "piwik.php" ]);
  _paq.push([ "setSiteId", "${siteid}" ]);
  var d = document, g = d.createElement("script"), s = d.getElementsByTagName("script")[0];
  g.type = "text/javascript";
  g.defer = true;
  g.async = true;
  g.src = u + "piwik.js";
  s.parentNode.insertBefore(g, s);
})();
</script>
<!-- End Piwik Code -->"""
		assert tagLib.trackPageview(customTrackingCode: "customTrackingCode();").toString() == expected
	}

	void testTrackPageviewCustomTrackingCodeAsStringAttrInConfig() {
		setConfigVariables([enabled : true, customTrackingCode : "customTrackingCode();"])

		def expected = """
<!-- Piwik -->
<script type="text/javascript">
var _paq = _paq || [];
customTrackingCode();
_paq.push([ "trackPageView" ]);
_paq.push([ "enableLinkTracking" ]);
(function() {
  var u = (("https:" == document.location.protocol) ? "https" : "http")
  + "://${piwikurl}";
  _paq.push([ "setTrackerUrl", u + "piwik.php" ]);
  _paq.push([ "setSiteId", "${siteid}" ]);
  var d = document, g = d.createElement("script"), s = d.getElementsByTagName("script")[0];
  g.type = "text/javascript";
  g.defer = true;
  g.async = true;
  g.src = u + "piwik.js";
  s.parentNode.insertBefore(g, s);
})();
</script>
<!-- End Piwik Code -->"""

		assert tagLib.trackPageview(customTrackingCode: "customTrackingCode();").toString() == expected
	}

	void testTrackPageviewCustomTrackingCodeAsListAttrInConfig() {
		setConfigVariables([enabled : true,
			customTrackingCode : [
				_setDetectFlash: true,
				_setDetectFlash: false,
				_setCampaignCookieTimeout: 31536000000,
				'custom': 'value',
				_trackPageview: null
			]])

		def expected = """
<!-- Piwik -->
<script type="text/javascript">
var _paq = _paq || [];
_paq.push(['_setClientInfo', true]);
_paq.push(['_setDetectFlash', false]);
_paq.push(['_setCampaignCookieTimeout', 31536000000]);
_paq.push(['custom', 'value']);
_paq.push(['_trackPageview']);
_paq.push([ "trackPageView" ]);
_paq.push([ "enableLinkTracking" ]);
(function() {
  var u = (("https:" == document.location.protocol) ? "https" : "http")
  + "://${piwikurl}";
  _paq.push([ "setTrackerUrl", u + "piwik.php" ]);
  _paq.push([ "setSiteId", "${siteid}" ]);
  var d = document, g = d.createElement("script"), s = d.getElementsByTagName("script")[0];
  g.type = "text/javascript";
  g.defer = true;
  g.async = true;
  g.src = u + "piwik.js";
  s.parentNode.insertBefore(g, s);
})();
</script>
<!-- End Piwik Code -->"""

		assert tagLib.trackPageview(customTrackingCode: [
			[_setClientInfo: true],
			[_setDetectFlash: false],
			[_setCampaignCookieTimeout: 31536000000],
			["custom": "value"],
			"_trackPageview"
		]).toString() == expected
	}

	// *****************************************************
	// * Helper
	// *****************************************************

	private setEnvironment(environment) {
		Environment.metaClass.static.getCurrent = { -> return environment }
	}

	private setConfigVariables(customParams = [:]){
		tagLib.grailsApplication.config = [
			piwik: [
				analytics:[ url : "${piwikurl}"] + [ siteid : "${siteid}"] + customParams
			]]
	}
}
