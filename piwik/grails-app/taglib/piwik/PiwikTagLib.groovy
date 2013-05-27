package piwik

import grails.util.Environment

class PiwikTagLib {

	static namespace = "piwik"

	def grailsApplication

	def trackPageview = {
		attrs ->
		if (isEnabled()) {
			out << """
<!-- Piwik -->
<script type="text/javascript">
var _paq = _paq || [];"""
			def customTrackingCode = attrs?.customTrackingCode ?: grailsApplication.config.piwik.analytics.customTrackingCode
			if (customTrackingCode instanceof String) {
				out << """
${customTrackingCode}"""
			}
			else if (customTrackingCode instanceof List && !customTrackingCode.isEmpty()) {
				customTrackingCode.each {
					if (it instanceof Map) {
						it.each {
							k, v ->
							if (v instanceof String) {
								out << """
_paq.push(['${k}', '${v}']);"""
							}
							else if (v instanceof Boolean) {
								out << """
_paq.push(['${k}', ${v}]);"""
							}
							else if (v) {
								out << """
_paq.push(['${k}', ${v}]);"""
							}
							else {
								out << """
_paq.push(['${k}']);"""
							}
						}
					}
					else {
						out << """
_paq.push(['${it}']);"""
					}
				}
			}

			out << """
_paq.push([ "trackPageView" ]);
_paq.push([ "enableLinkTracking" ]);
(function() {
  var u = (("https:" == document.location.protocol) ? "https" : "http")
  + "://${getPiwikURL()}";
  _paq.push([ "setTrackerUrl", u + "piwik.php" ]);
  _paq.push([ "setSiteId", "${getSiteId()}" ]);
  var d = document, g = d.createElement("script"), s = d.getElementsByTagName("script")[0];
  g.type = "text/javascript";
  g.defer = true;
  g.async = true;
  g.src = u + "piwik.js";
  s.parentNode.insertBefore(g, s);
})();
</script>
<!-- End Piwik Code -->"""
		}
	}

	private isEnabled() {
		def enabled = grailsApplication.config.piwik.analytics.enabled

		// disable piwik analytics if url or site id is not defined
			if (!getPiwikURL() || !getSiteId()) {
				enabled = false
			}
			else {
				// enable piwik analytics by default for production environment
				if (!(enabled instanceof Boolean) && Environment.current == Environment.PRODUCTION) {
					enabled = true
				}
			}

			return enabled
		}

		private getPiwikURL() {
			return grailsApplication.config.piwik.analytics.url
		}

		private getSiteId() {
			return grailsApplication.config.piwik.analytics.siteid
		}
	}
