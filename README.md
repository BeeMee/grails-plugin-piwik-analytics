grails-plugin-piwik-analytics
=============================

A simple grails plugin to include piwik tracking code


<h2>Summary</h2>
                <div class="wiki-plugin">
                    Include piwik analyics script
                </div>
                
                <h2>Installation</h2>
                <div class="wiki-plugin">
                    <div class="code"><pre>grails install-plugin piwik-analytics</pre></div>
                </div>
                
                
                <h2>Description</h2>
                <div class="wiki-plugin">
                    <a name="Introduction"></a><h2>Introduction</h2><p class="paragraph">This plugin provides a simple taglib to embed Piwik Analytics pageview tracking to your Grails application.</p><p class="paragraph">
<a name="Usage"></a></p><h2>Usage</h2><p class="paragraph"><a name="Add Piwik URL and Site ID to Configuration"></a></p><h3>Add Piwik URL and Site ID to Configuration</h3><p class="paragraph">Add your Piwik URL and Site ID to grails-app/config/Config.groovy:</p><p class="paragraph"></p><div class="code"><pre>piwik.analytics.url = <span class="java-quote">"http://example.com/piwik"</span><br/>piwik.analytics.siteid = <span class="java-quote">1</span></pre></div><p class="paragraph">
<a name="Include Piwik Analytics tracking code to your page"></a></p><h3>Include Piwik Analytics tracking code to your page</h3><p class="paragraph">Add the &lt;piwik:trackPageview /&gt; tag to your view(s). If you want all your pages to include the tracking code, just add it to the main.gsp layout. As recommended by Piwik place this as last script in the &lt;head&gt; section.</p><p class="paragraph"></p><div class="code"><pre>&lt;html&gt;
    &lt;head&gt;
        ..
        &lt;piwik:trackPageview /&gt;
    &lt;/head&gt;
    &lt;body&gt;
        ..
    &lt;/body&gt;
&lt;/html&gt;</pre></div><p class="paragraph"><a name="Sensible Defaults"></a></p><h2>Sensible Defaults</h2><p class="paragraph">The plugin uses sensible defaults. By default, when adding &lt;piwik:trackPageview /&gt; to your views/layouts, only running in production will output the tracking code. Thus not in development and test.</p><p class="paragraph">This behaviour can be overridden by explicitly enabling/disabling Piwik Analytics in Config.groovy.</p><p class="paragraph"></p><div class="code"><pre>piwik.analytics.enabled = <span class="java-keyword">true</span></pre></div><p class="paragraph">or</p><p class="paragraph"></p><div class="code"><pre>piwik.analytics.enabled = <span class="java-keyword">false</span></pre></div>

<!--p class="paragraph"><a name="Asynchronous vs. Traditional tracking code"></a></p><h2>Asynchronous vs. Traditional tracking code</h2><p class="paragraph">Since version 1.0 of this plugin asynchronous tracking code is used by default when using &lt;piwik:trackPageview /&gt;.
If you want to use the old traditional tracking code code instead add this to grails-app/config/Config.groovy:</p><p class="paragraph"></p><div class="code"><pre>piwik.analytics.traditional = <span class="java-keyword">true</span></pre></div><p class="paragraph">For traditional tracking code, make sure you put the tag just before the closing &lt;/body&gt; tag instead of in the &lt;head&gt; element.</p><p class="paragraph"></p><div class="code"><pre>&lt;html&gt;
    &lt;head&gt;
        ..
    &lt;/head&gt;
    &lt;body&gt;
        ..
        &lt;piwik:trackPageview /&gt;
    &lt;/body&gt;
&lt;/html&gt;</pre></div><p class="paragraph">Note that the plugin also offers &lt;piwik:trackPageviewAsynch /&gt; and &lt;piwik:trackPageviewTraditional /&gt; tags to use the type of tracking code explicitly. This is mainly for backwards compatibility as the &lt;piwik:trackPageviewAsynch /&gt; was needed for asynchronous tracking code prior to version 1.0 of this plugin.</p-->

<p class="paragraph"><a name="Tracking Customizations"></a></p><h2>Tracking Customizations</h2><p class="paragraph">If you want to customize the tracking code you can either provide the customization in grails-app/config/Config.groovy or in the tag itself. The customization can be a String of javascript code or a smart List with tracking code. The examples below speak for themselves.</p><p class="paragraph"><a name="Simple String configuration in Config.groovy"></a></p><h4>Simple String configuration in Config.groovy</h4><p class="paragraph"></p><div class="code"><pre>piwik.analytics.customTrackingCode = <span class="java-quote">"_paq.push(['trackGoal', <span class="java-keyword">1</span>]); _paq.push(['enableLinkTracking']);"</span></pre></div><p class="paragraph">Note that you have the trackPageview manually when using any custom tracking code.</p><p class="paragraph"><a name="List configuration in Config.groovy"></a></p><h4>List configuration in Config.groovy</h4><p class="paragraph"></p><div class="code"><pre>piwik.analytics.customTrackingCode = [
    [trackGoal: <span class="java-keyword">1</span>],
    <span class="java-quote">"enableLinkTracking"</span>
]</pre></div><p class="paragraph"><a name="Custom code directly in tag"></a></p><h4>Custom code directly in tag</h4><p class="paragraph"></p><div class="code"><pre>&lt;piwik:trackPageview customTrackingCode=<span class="java-quote">"_paq.push(['trackGoal', <span class="java-keyword">1</span>]); _paq.push(['enableLinkTracking']);"</span> /&gt;</pre></div><p class="paragraph">Note that you can also provide a similar List to the tag itself as in the other example.</p><p class="paragraph"><a name="Version History"></a></p><h2>Version History</h2>
<ul class="star">
<li><strong class="bold">0.1</strong></li>
<ul class="star">
<li>First official release</li>
</ul></ul>