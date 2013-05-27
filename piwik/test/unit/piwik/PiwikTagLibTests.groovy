package piwik



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
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

		assert tagLib.trackPageviewAsynch() == ""
	}

	void testTrackPageviewExplicitlyEnabledInDevelopment() {
		setEnvironment(Environment.DEVELOPMENT)
		setConfigVariables([enabled : true])

		assert tagLib.trackPageviewAsynch().toString() == expectedAsynch
	}

	void testTrackPageviewDefaultDisabledInTest() {
		setEnvironment(Environment.TEST)
		setConfigVariables()

		assert tagLib.trackPageviewAsynch() == ""
	}

	void testTrackPageviewExplicitlyEnabledInTest() {
		setEnvironment(Environment.TEST)
		setConfigVariables([enabled : true])

		assert tagLib.trackPageviewAsynch().toString() == expectedAsynch
	}

	void testTrackPageviewDefaultEnabledInProduction() {
		setEnvironment(Environment.PRODUCTION)
		setConfigVariables()

		assert tagLib.trackPageviewAsynch().toString() == expectedAsynch
	}

	void testTrackPageviewExplicitlyDisabledInProduction() {
		setEnvironment(Environment.PRODUCTION)
		setConfigVariables([enabled : false])

		assert tagLib.trackPageviewAsynch() == ""
	}

	void testTrackPageviewEnabled() {
		setConfigVariables([enabled : true])

		assert tagLib.trackPageviewAsynch().toString() == expectedAsynch
	}

	void testTrackPageviewDisabled() {
		setConfigVariables([enabled : false])

		assert tagLib.trackPageviewAsynch() == ""
	}

	void testTrackPageviewNoWebPropertyIDButExplicitlyEnabled() {
		setConfigVariables([enabled : true, webPropertyID: null])

		assert tagLib.trackPageviewAsynch() == ""
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
		assert tagLib.trackPageviewAsynch(customTrackingCode: "customTrackingCode();").toString() == expected
	}

	void testTrackPageviewCustomTrackingCodeAsListAttr() {
		setConfigVariables([enabled : true])

		def expected = """
<!-- Piwik -->
<script type="text/javascript">
var _paq = _paq || [];
_paq.push(['setDocumentTitle', document.title]);
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
</script>"""
		
		assert tagLib.trackPageviewAsynch(customTrackingCode: [
			[setDocumentTitle: document.title]
		]).toString() == expected
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

		assert tagLib.trackPageviewAsynch(customTrackingCode: "customTrackingCode();").toString() == expected
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
_paq.push(['_setAccount', '${webPropertyID}']);
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

		assert tagLib.trackPageviewAsynch(customTrackingCode: [
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
