



<!DOCTYPE html>
<html>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
 <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" >
 
 <meta name="ROBOTS" content="NOARCHIVE">
 
 <link rel="icon" type="image/vnd.microsoft.icon" href="http://www.gstatic.com/codesite/ph/images/phosting.ico">
 
 
 <script type="text/javascript">
 
 
 
 
 var codesite_token = "nZW5oObCK9ko18gW1fBg8d_BkyA:1382005184567";
 
 
 var CS_env = {"domainName":null,"relativeBaseUrl":"","loggedInUserEmail":"akis100@gmail.com","assetHostPath":"http://www.gstatic.com/codesite/ph","assetVersionPath":"http://www.gstatic.com/codesite/ph/9670661675484913303","projectHomeUrl":"/p/jmonkeyengine","profileUrl":"/u/116090281484404608661/","projectName":"jmonkeyengine","token":"nZW5oObCK9ko18gW1fBg8d_BkyA:1382005184567"};
 var _gaq = _gaq || [];
 _gaq.push(
 ['siteTracker._setAccount', 'UA-18071-1'],
 ['siteTracker._trackPageview']);
 
 _gaq.push(
 ['projectTracker._setAccount', 'UA-10669913-2'],
 ['projectTracker._trackPageview']);
 
 (function() {
 var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
 ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
 (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(ga);
 })();
 
 </script>
 
 
 <title>SimpleTextured.frag - 
 jmonkeyengine -
 
 
 jMonkeyEngine is a modern 3D game engine written entirely in Java - Google Project Hosting
 </title>
 <link type="text/css" rel="stylesheet" href="http://www.gstatic.com/codesite/ph/9670661675484913303/css/core.css">
 
 <link type="text/css" rel="stylesheet" href="http://www.gstatic.com/codesite/ph/9670661675484913303/css/ph_detail.css" >
 
 
 <link type="text/css" rel="stylesheet" href="http://www.gstatic.com/codesite/ph/9670661675484913303/css/d_sb.css" >
 
 
 
<!--[if IE]>
 <link type="text/css" rel="stylesheet" href="http://www.gstatic.com/codesite/ph/9670661675484913303/css/d_ie.css" >
<![endif]-->
 <style type="text/css">
 .menuIcon.off { background: no-repeat url(http://www.gstatic.com/codesite/ph/images/dropdown_sprite.gif) 0 -42px }
 .menuIcon.on { background: no-repeat url(http://www.gstatic.com/codesite/ph/images/dropdown_sprite.gif) 0 -28px }
 .menuIcon.down { background: no-repeat url(http://www.gstatic.com/codesite/ph/images/dropdown_sprite.gif) 0 0; }
 
 
 
  tr.inline_comment {
 background: #fff;
 vertical-align: top;
 }
 div.draft, div.published {
 padding: .3em;
 border: 1px solid #999; 
 margin-bottom: .1em;
 font-family: arial, sans-serif;
 max-width: 60em;
 }
 div.draft {
 background: #ffa;
 } 
 div.published {
 background: #e5ecf9;
 }
 div.published .body, div.draft .body {
 padding: .5em .1em .1em .1em;
 max-width: 60em;
 white-space: pre-wrap;
 white-space: -moz-pre-wrap;
 white-space: -pre-wrap;
 white-space: -o-pre-wrap;
 word-wrap: break-word;
 font-size: 1em;
 }
 div.draft .actions {
 margin-left: 1em;
 font-size: 90%;
 }
 div.draft form {
 padding: .5em .5em .5em 0;
 }
 div.draft textarea, div.published textarea {
 width: 95%;
 height: 10em;
 font-family: arial, sans-serif;
 margin-bottom: .5em;
 }

 
 .nocursor, .nocursor td, .cursor_hidden, .cursor_hidden td {
 background-color: white;
 height: 2px;
 }
 .cursor, .cursor td {
 background-color: darkblue;
 height: 2px;
 display: '';
 }
 
 
.list {
 border: 1px solid white;
 border-bottom: 0;
}

 
 </style>
</head>
<body class="t4">
<script type="text/javascript">
 window.___gcfg = {lang: 'en'};
 (function() 
 {var po = document.createElement("script");
 po.type = "text/javascript"; po.async = true;po.src = "https://apis.google.com/js/plusone.js";
 var s = document.getElementsByTagName("script")[0];
 s.parentNode.insertBefore(po, s);
 })();
</script>
<div class="headbg">

 <div id="gaia">
 

 <span>
 
 
 
 <a href="#" id="multilogin-dropdown" onclick="return false;"
 ><u><b>akis100@gmail.com</b></u> <small>&#9660;</small></a>
 
 
 | <a href="/u/116090281484404608661/" id="projects-dropdown" onclick="return false;"
 ><u>My favorites</u> <small>&#9660;</small></a>
 | <a href="/u/116090281484404608661/" onclick="_CS_click('/gb/ph/profile');"
 title="Profile, Updates, and Settings"
 ><u>Profile</u></a>
 | <a href="https://www.google.com/accounts/Logout?continue=http%3A%2F%2Fcode.google.com%2Fp%2Fjmonkeyengine%2Fsource%2Fbrowse%2Fbranches%2Fjme3%2Fsrc%2Fcore-data%2FCommon%2FMatDefs%2FMisc%2FSimpleTextured.frag%3Fr%3D5108" 
 onclick="_CS_click('/gb/ph/signout');"
 ><u>Sign out</u></a>
 
 </span>

 </div>

 <div class="gbh" style="left: 0pt;"></div>
 <div class="gbh" style="right: 0pt;"></div>
 
 
 <div style="height: 1px"></div>
<!--[if lte IE 7]>
<div style="text-align:center;">
Your version of Internet Explorer is not supported. Try a browser that
contributes to open source, such as <a href="http://www.firefox.com">Firefox</a>,
<a href="http://www.google.com/chrome">Google Chrome</a>, or
<a href="http://code.google.com/chrome/chromeframe/">Google Chrome Frame</a>.
</div>
<![endif]-->



 <table style="padding:0px; margin: 0px 0px 10px 0px; width:100%" cellpadding="0" cellspacing="0"
 itemscope itemtype="http://schema.org/CreativeWork">
 <tr style="height: 58px;">
 
 
 
 <td id="plogo">
 <link itemprop="url" href="/p/jmonkeyengine">
 <a href="/p/jmonkeyengine/">
 
 
 <img src="/p/jmonkeyengine/logo?cct=1366223005"
 alt="Logo" itemprop="image">
 
 </a>
 </td>
 
 <td style="padding-left: 0.5em">
 
 <div id="pname">
 <a href="/p/jmonkeyengine/"><span itemprop="name">jmonkeyengine</span></a>
 </div>
 
 <div id="psum">
 <a id="project_summary_link"
 href="/p/jmonkeyengine/"><span itemprop="description">jMonkeyEngine is a modern 3D game engine written entirely in Java</span></a>
 
 </div>
 
 
 </td>
 <td style="white-space:nowrap;text-align:right; vertical-align:bottom;">
 
 <form action="/hosting/search">
 <input size="30" name="q" value="" type="text">
 
 <input type="submit" name="projectsearch" value="Search projects" >
 </form>
 
 </tr>
 </table>

</div>

 
<div id="mt" class="gtb"> 
 <a href="/p/jmonkeyengine/" class="tab ">Project&nbsp;Home</a>
 
 
 
 
 <a href="/p/jmonkeyengine/downloads/list" class="tab ">Downloads</a>
 
 
 
 
 
 <a href="/p/jmonkeyengine/wiki/Introduction?tm=6" class="tab ">Wiki</a>
 
 
 
 
 
 <a href="/p/jmonkeyengine/wiki/IssueFrontpage?tm=3"
 class="tab ">Issues</a>
 
 
 
 
 
 <a href="/p/jmonkeyengine/source/checkout"
 class="tab active">Source</a>
 
 
 
 
 
 
 
 
 <div class=gtbc></div>
</div>
<table cellspacing="0" cellpadding="0" width="100%" align="center" border="0" class="st">
 <tr>
 
 
 
 
 
 
 <td class="subt">
 <div class="st2">
 <div class="isf">
 
 


 <span class="inst1"><a href="/p/jmonkeyengine/source/checkout">Checkout</a></span> &nbsp;
 <span class="inst2"><a href="/p/jmonkeyengine/source/browse/">Browse</a></span> &nbsp;
 <span class="inst3"><a href="/p/jmonkeyengine/source/list">Changes</a></span> &nbsp;
 
 
 
 
 
 
 
 </form>
 <script type="text/javascript">
 
 function codesearchQuery(form) {
 var query = document.getElementById('q').value;
 if (query) { form.action += '%20' + query; }
 }
 </script>
 </div>
</div>

 </td>
 
 
 
 <td align="right" valign="top" class="bevel-right"></td>
 </tr>
</table>


<script type="text/javascript">
 var cancelBubble = false;
 function _go(url) { document.location = url; }
</script>
<div id="maincol"
 
>

 




<div class="expand">
<div id="colcontrol">
<style type="text/css">
 #file_flipper { white-space: nowrap; padding-right: 2em; }
 #file_flipper.hidden { display: none; }
 #file_flipper .pagelink { color: #0000CC; text-decoration: underline; }
 #file_flipper #visiblefiles { padding-left: 0.5em; padding-right: 0.5em; }
</style>
<table id="nav_and_rev" class="list"
 cellpadding="0" cellspacing="0" width="100%">
 <tr>
 
 <td nowrap="nowrap" class="src_crumbs src_nav" width="33%">
 <strong class="src_nav">Source path:&nbsp;</strong>
 <span id="crumb_root">
 
 <a href="/p/jmonkeyengine/source/browse/?r=5108">svn</a>/&nbsp;</span>
 <span id="crumb_links" class="ifClosed"><a href="/p/jmonkeyengine/source/browse/branches/?r=5108">branches</a><span class="sp">/&nbsp;</span><a href="/p/jmonkeyengine/source/browse/branches/jme3/?r=5108">jme3</a><span class="sp">/&nbsp;</span><a href="/p/jmonkeyengine/source/browse/branches/jme3/src/?r=5108">src</a><span class="sp">/&nbsp;</span><a href="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/?r=5108">core-data</a><span class="sp">/&nbsp;</span><a href="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/?r=5108">Common</a><span class="sp">/&nbsp;</span><a href="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/?r=5108">MatDefs</a><span class="sp">/&nbsp;</span><a href="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/?r=5108">Misc</a><span class="sp">/&nbsp;</span>SimpleTextured.frag</span>
 
 


 </td>
 
 
 <td nowrap="nowrap" width="33%" align="right">
 <table cellpadding="0" cellspacing="0" style="font-size: 100%"><tr>
 
 
 <td class="flipper"><b>r5108</b></td>
 
 </tr></table>
 </td> 
 </tr>
</table>

<div class="fc">
 
 
 
<style type="text/css">
.undermouse span {
 background-image: url(http://www.gstatic.com/codesite/ph/images/comments.gif); }
</style>
<table class="opened" id="review_comment_area"
><tr>
<td id="nums">
<pre><table width="100%"><tr class="nocursor"><td></td></tr></table></pre>
<pre><table width="100%" id="nums_table_0"><tr id="gr_svn5108_1"

><td id="1"><a href="#1">1</a></td></tr
><tr id="gr_svn5108_2"

><td id="2"><a href="#2">2</a></td></tr
><tr id="gr_svn5108_3"

><td id="3"><a href="#3">3</a></td></tr
><tr id="gr_svn5108_4"

><td id="4"><a href="#4">4</a></td></tr
><tr id="gr_svn5108_5"

><td id="5"><a href="#5">5</a></td></tr
><tr id="gr_svn5108_6"

><td id="6"><a href="#6">6</a></td></tr
><tr id="gr_svn5108_7"

><td id="7"><a href="#7">7</a></td></tr
><tr id="gr_svn5108_8"

><td id="8"><a href="#8">8</a></td></tr
><tr id="gr_svn5108_9"

><td id="9"><a href="#9">9</a></td></tr
><tr id="gr_svn5108_10"

><td id="10"><a href="#10">10</a></td></tr
><tr id="gr_svn5108_11"

><td id="11"><a href="#11">11</a></td></tr
><tr id="gr_svn5108_12"

><td id="12"><a href="#12">12</a></td></tr
><tr id="gr_svn5108_13"

><td id="13"><a href="#13">13</a></td></tr
><tr id="gr_svn5108_14"

><td id="14"><a href="#14">14</a></td></tr
><tr id="gr_svn5108_15"

><td id="15"><a href="#15">15</a></td></tr
><tr id="gr_svn5108_16"

><td id="16"><a href="#16">16</a></td></tr
><tr id="gr_svn5108_17"

><td id="17"><a href="#17">17</a></td></tr
><tr id="gr_svn5108_18"

><td id="18"><a href="#18">18</a></td></tr
><tr id="gr_svn5108_19"

><td id="19"><a href="#19">19</a></td></tr
><tr id="gr_svn5108_20"

><td id="20"><a href="#20">20</a></td></tr
><tr id="gr_svn5108_21"

><td id="21"><a href="#21">21</a></td></tr
><tr id="gr_svn5108_22"

><td id="22"><a href="#22">22</a></td></tr
><tr id="gr_svn5108_23"

><td id="23"><a href="#23">23</a></td></tr
><tr id="gr_svn5108_24"

><td id="24"><a href="#24">24</a></td></tr
><tr id="gr_svn5108_25"

><td id="25"><a href="#25">25</a></td></tr
><tr id="gr_svn5108_26"

><td id="26"><a href="#26">26</a></td></tr
><tr id="gr_svn5108_27"

><td id="27"><a href="#27">27</a></td></tr
></table></pre>
<pre><table width="100%"><tr class="nocursor"><td></td></tr></table></pre>
</td>
<td id="lines">
<pre><table width="100%"><tr class="cursor_stop cursor_hidden"><td></td></tr></table></pre>
<pre ><table id="src_table_0"><tr
id=sl_svn5108_1

><td class="source">#import &quot;Common/ShaderLib/Texture.glsllib&quot;<br></td></tr
><tr
id=sl_svn5108_2

><td class="source"><br></td></tr
><tr
id=sl_svn5108_3

><td class="source">varying vec2 texCoord;<br></td></tr
><tr
id=sl_svn5108_4

><td class="source"><br></td></tr
><tr
id=sl_svn5108_5

><td class="source">uniform sampler2D m_ColorMap;<br></td></tr
><tr
id=sl_svn5108_6

><td class="source"><br></td></tr
><tr
id=sl_svn5108_7

><td class="source">void main(){<br></td></tr
><tr
id=sl_svn5108_8

><td class="source">    //Texture_GetColor(m_ColorMap, texCoord)<br></td></tr
><tr
id=sl_svn5108_9

><td class="source">    //vec4 color = texture2D(m_ColorMap, texCoord);<br></td></tr
><tr
id=sl_svn5108_10

><td class="source">    //color.rgb *= color.a;<br></td></tr
><tr
id=sl_svn5108_11

><td class="source">    //gl_FragColor = vec4(color.a);<br></td></tr
><tr
id=sl_svn5108_12

><td class="source"><br></td></tr
><tr
id=sl_svn5108_13

><td class="source">    #ifdef NORMAL_LATC<br></td></tr
><tr
id=sl_svn5108_14

><td class="source">        vec3 newNorm = vec3(texture2D(m_ColorMap, texCoord).ag, 0.0);<br></td></tr
><tr
id=sl_svn5108_15

><td class="source">        newNorm = Common_UnpackNormal(newNorm);<br></td></tr
><tr
id=sl_svn5108_16

><td class="source">        newNorm.b = sqrt(1.0 - (newNorm.x * newNorm.x) - (newNorm.y * newNorm.y));<br></td></tr
><tr
id=sl_svn5108_17

><td class="source">        newNorm = Common_PackNormal(newNorm);<br></td></tr
><tr
id=sl_svn5108_18

><td class="source">        gl_FragColor = vec4(newNorm, 1.0);<br></td></tr
><tr
id=sl_svn5108_19

><td class="source">    #elif defined(SHOW_ALPHA)<br></td></tr
><tr
id=sl_svn5108_20

><td class="source">        gl_FragColor = vec4(texture2D(m_ColorMap, texCoord).a);<br></td></tr
><tr
id=sl_svn5108_21

><td class="source">    #else<br></td></tr
><tr
id=sl_svn5108_22

><td class="source">        gl_FragColor = Texture_GetColor(m_ColorMap, texCoord);<br></td></tr
><tr
id=sl_svn5108_23

><td class="source">    #endif<br></td></tr
><tr
id=sl_svn5108_24

><td class="source">    #ifdef NORMALIZE<br></td></tr
><tr
id=sl_svn5108_25

><td class="source">        gl_FragColor = vec4(normalize(gl_FragColor.xyz), gl_FragColor.a);<br></td></tr
><tr
id=sl_svn5108_26

><td class="source">    #endif<br></td></tr
><tr
id=sl_svn5108_27

><td class="source">}<br></td></tr
></table></pre>
<pre><table width="100%"><tr class="cursor_stop cursor_hidden"><td></td></tr></table></pre>
</td>
</tr></table>

 
<script type="text/javascript">
 var lineNumUnderMouse = -1;
 
 function gutterOver(num) {
 gutterOut();
 var newTR = document.getElementById('gr_svn5108_' + num);
 if (newTR) {
 newTR.className = 'undermouse';
 }
 lineNumUnderMouse = num;
 }
 function gutterOut() {
 if (lineNumUnderMouse != -1) {
 var oldTR = document.getElementById(
 'gr_svn5108_' + lineNumUnderMouse);
 if (oldTR) {
 oldTR.className = '';
 }
 lineNumUnderMouse = -1;
 }
 }
 var numsGenState = {table_base_id: 'nums_table_'};
 var srcGenState = {table_base_id: 'src_table_'};
 var alignerRunning = false;
 var startOver = false;
 function setLineNumberHeights() {
 if (alignerRunning) {
 startOver = true;
 return;
 }
 numsGenState.chunk_id = 0;
 numsGenState.table = document.getElementById('nums_table_0');
 numsGenState.row_num = 0;
 if (!numsGenState.table) {
 return; // Silently exit if no file is present.
 }
 srcGenState.chunk_id = 0;
 srcGenState.table = document.getElementById('src_table_0');
 srcGenState.row_num = 0;
 alignerRunning = true;
 continueToSetLineNumberHeights();
 }
 function rowGenerator(genState) {
 if (genState.row_num < genState.table.rows.length) {
 var currentRow = genState.table.rows[genState.row_num];
 genState.row_num++;
 return currentRow;
 }
 var newTable = document.getElementById(
 genState.table_base_id + (genState.chunk_id + 1));
 if (newTable) {
 genState.chunk_id++;
 genState.row_num = 0;
 genState.table = newTable;
 return genState.table.rows[0];
 }
 return null;
 }
 var MAX_ROWS_PER_PASS = 1000;
 function continueToSetLineNumberHeights() {
 var rowsInThisPass = 0;
 var numRow = 1;
 var srcRow = 1;
 while (numRow && srcRow && rowsInThisPass < MAX_ROWS_PER_PASS) {
 numRow = rowGenerator(numsGenState);
 srcRow = rowGenerator(srcGenState);
 rowsInThisPass++;
 if (numRow && srcRow) {
 if (numRow.offsetHeight != srcRow.offsetHeight) {
 numRow.firstChild.style.height = srcRow.offsetHeight + 'px';
 }
 }
 }
 if (rowsInThisPass >= MAX_ROWS_PER_PASS) {
 setTimeout(continueToSetLineNumberHeights, 10);
 } else {
 alignerRunning = false;
 if (startOver) {
 startOver = false;
 setTimeout(setLineNumberHeights, 500);
 }
 }
 }
 function initLineNumberHeights() {
 // Do 2 complete passes, because there can be races
 // between this code and prettify.
 startOver = true;
 setTimeout(setLineNumberHeights, 250);
 window.onresize = setLineNumberHeights;
 }
 initLineNumberHeights();
</script>

 
 
 <div id="log">
 <div style="text-align:right">
 <a class="ifCollapse" href="#" onclick="_toggleMeta(this); return false">Show details</a>
 <a class="ifExpand" href="#" onclick="_toggleMeta(this); return false">Hide details</a>
 </div>
 <div class="ifExpand">
 
 
 <div class="pmeta_bubble_bg" style="border:1px solid white">
 <div class="round4"></div>
 <div class="round2"></div>
 <div class="round1"></div>
 <div class="box-inner">
 <div id="changelog">
 <p>Change log</p>
 <div>
 <a href="/p/jmonkeyengine/source/detail?spec=svn5108&amp;r=5108">r5108</a>
 by shadowislord
 on Apr 20, 2010
 &nbsp; <a href="/p/jmonkeyengine/source/diff?spec=svn5108&r=5108&amp;format=side&amp;path=/branches/jme3/src/core-data/Common/MatDefs/Misc/SimpleTextured.frag&amp;old_path=/branches/jme3/src/core-data/Common/MatDefs/Misc/SimpleTextured.frag&amp;old=">Diff</a>
 </div>
 <pre>[No log message]</pre>
 </div>
 
 
 
 
 
 
 <script type="text/javascript">
 var detail_url = '/p/jmonkeyengine/source/detail?r=5108&spec=svn5108';
 var publish_url = '/p/jmonkeyengine/source/detail?r=5108&spec=svn5108#publish';
 // describe the paths of this revision in javascript.
 var changed_paths = [];
 var changed_urls = [];
 
 changed_paths.push('/branches/jme3/src/core-data/Common');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Gui');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Gui?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Gui/Gui.frag');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Gui/Gui.frag?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Gui/Gui.j3md');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Gui/Gui.j3md?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Gui/Gui.vert');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Gui/Gui.vert?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Hdr');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Hdr?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Hdr/LogLum.frag');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Hdr/LogLum.frag?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Hdr/LogLum.j3md');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Hdr/LogLum.j3md?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Hdr/ToneMap.frag');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Hdr/ToneMap.frag?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Hdr/ToneMap.j3md');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Hdr/ToneMap.j3md?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Light');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Light?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Light/Lighting.frag');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Light/Lighting.frag?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Light/Lighting.j3md');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Light/Lighting.j3md?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Light/Lighting.vert');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Light/Lighting.vert?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Light/LightingOld.frag');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Light/LightingOld.frag?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Light/LightingOld.vert');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Light/LightingOld.vert?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Light/Reflection.frag');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Light/Reflection.frag?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Light/Reflection.j3md');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Light/Reflection.j3md?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Light/Reflection.vert');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Light/Reflection.vert?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Misc');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Misc/Particle.frag');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/Particle.frag?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Misc/Particle.j3md');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/Particle.j3md?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Misc/Particle.vert');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/Particle.vert?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Misc/ShowNormals.frag');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/ShowNormals.frag?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Misc/ShowNormals.j3md');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/ShowNormals.j3md?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Misc/ShowNormals.vert');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/ShowNormals.vert?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Misc/SimpleTextured.frag');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/SimpleTextured.frag?r\x3d5108\x26spec\x3dsvn5108');
 
 var selected_path = '/branches/jme3/src/core-data/Common/MatDefs/Misc/SimpleTextured.frag';
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Misc/SimpleTextured.j3md');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/SimpleTextured.j3md?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Misc/SimpleTextured.vert');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/SimpleTextured.vert?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Misc/Sky.frag');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/Sky.frag?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Misc/Sky.j3md');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/Sky.j3md?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Misc/Sky.vert');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/Sky.vert?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Misc/SolidColor.frag');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/SolidColor.frag?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Misc/SolidColor.j3md');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/SolidColor.j3md?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Misc/SolidColor.vert');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/SolidColor.vert?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Misc/VertexColor.frag');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/VertexColor.frag?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Misc/VertexColor.j3md');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/VertexColor.j3md?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Misc/VertexColor.vert');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/VertexColor.vert?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Misc/WireColor.j3md');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/WireColor.j3md?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Shadow');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Shadow?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Shadow/PostShadow.frag');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Shadow/PostShadow.frag?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Shadow/PostShadow.j3md');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Shadow/PostShadow.j3md?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Shadow/PostShadow.vert');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Shadow/PostShadow.vert?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Shadow/PreShadow.frag');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Shadow/PreShadow.frag?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Shadow/PreShadow.j3md');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Shadow/PreShadow.j3md?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/MatDefs/Shadow/PreShadow.vert');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Shadow/PreShadow.vert?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/Materials');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/Materials?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/Materials/RedColor.j3m');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/Materials/RedColor.j3m?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/Materials/VertexColor.j3m');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/Materials/VertexColor.j3m?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/Materials/WhiteColor.j3m');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/Materials/WhiteColor.j3m?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/ShaderLib');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/ShaderLib?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/ShaderLib/Bump.glsllib');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/ShaderLib/Bump.glsllib?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/ShaderLib/Common.glsllib');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/ShaderLib/Common.glsllib?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/ShaderLib/Hdr.glsllib');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/ShaderLib/Hdr.glsllib?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/ShaderLib/Lighting.glsllib');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/ShaderLib/Lighting.glsllib?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/ShaderLib/Math.glsllib');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/ShaderLib/Math.glsllib?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/ShaderLib/Optics.glsllib');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/ShaderLib/Optics.glsllib?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/ShaderLib/Shadow.glsllib');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/ShaderLib/Shadow.glsllib?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/ShaderLib/Skinning.glsllib');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/ShaderLib/Skinning.glsllib?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/ShaderLib/Tangent.glsllib');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/ShaderLib/Tangent.glsllib?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/ShaderLib/Texture.glsllib');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/ShaderLib/Texture.glsllib?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Common/ShaderLib/Ubo.glsllib');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/ShaderLib/Ubo.glsllib?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Interface');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Interface?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Interface/Fonts');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Interface/Fonts?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Interface/Fonts/Console.fnt');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Interface/Fonts/Console.fnt?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Interface/Fonts/Console.png');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Interface/Fonts/Console.png?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Interface/Fonts/Default.fnt');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Interface/Fonts/Default.fnt?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/Interface/Fonts/Default.png');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Interface/Fonts/Default.png?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/fonts');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/fonts?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/matdefs');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/matdefs?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/shaderlib');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/shaderlib?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/shaders');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/shaders?r\x3d5108\x26spec\x3dsvn5108');
 
 
 changed_paths.push('/branches/jme3/src/core-data/textures');
 changed_urls.push('/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/textures?r\x3d5108\x26spec\x3dsvn5108');
 
 
 function getCurrentPageIndex() {
 for (var i = 0; i < changed_paths.length; i++) {
 if (selected_path == changed_paths[i]) {
 return i;
 }
 }
 }
 function getNextPage() {
 var i = getCurrentPageIndex();
 if (i < changed_paths.length - 1) {
 return changed_urls[i + 1];
 }
 return null;
 }
 function getPreviousPage() {
 var i = getCurrentPageIndex();
 if (i > 0) {
 return changed_urls[i - 1];
 }
 return null;
 }
 function gotoNextPage() {
 var page = getNextPage();
 if (!page) {
 page = detail_url;
 }
 window.location = page;
 }
 function gotoPreviousPage() {
 var page = getPreviousPage();
 if (!page) {
 page = detail_url;
 }
 window.location = page;
 }
 function gotoDetailPage() {
 window.location = detail_url;
 }
 function gotoPublishPage() {
 window.location = publish_url;
 }
</script>

 
 <style type="text/css">
 #review_nav {
 border-top: 3px solid white;
 padding-top: 6px;
 margin-top: 1em;
 }
 #review_nav td {
 vertical-align: middle;
 }
 #review_nav select {
 margin: .5em 0;
 }
 </style>
 <div id="review_nav">
 <table><tr><td>Go to:&nbsp;</td><td>
 <select name="files_in_rev" onchange="window.location=this.value">
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common?r=5108&amp;spec=svn5108"
 
 >/branches/jme3/src/core-data/Common</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs?r=5108&amp;spec=svn5108"
 
 >...me3/src/core-data/Common/MatDefs</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Gui?r=5108&amp;spec=svn5108"
 
 >...src/core-data/Common/MatDefs/Gui</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Gui/Gui.frag?r=5108&amp;spec=svn5108"
 
 >...data/Common/MatDefs/Gui/Gui.frag</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Gui/Gui.j3md?r=5108&amp;spec=svn5108"
 
 >...data/Common/MatDefs/Gui/Gui.j3md</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Gui/Gui.vert?r=5108&amp;spec=svn5108"
 
 >...data/Common/MatDefs/Gui/Gui.vert</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Hdr?r=5108&amp;spec=svn5108"
 
 >...src/core-data/Common/MatDefs/Hdr</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Hdr/LogLum.frag?r=5108&amp;spec=svn5108"
 
 >...a/Common/MatDefs/Hdr/LogLum.frag</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Hdr/LogLum.j3md?r=5108&amp;spec=svn5108"
 
 >...a/Common/MatDefs/Hdr/LogLum.j3md</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Hdr/ToneMap.frag?r=5108&amp;spec=svn5108"
 
 >.../Common/MatDefs/Hdr/ToneMap.frag</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Hdr/ToneMap.j3md?r=5108&amp;spec=svn5108"
 
 >.../Common/MatDefs/Hdr/ToneMap.j3md</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Light?r=5108&amp;spec=svn5108"
 
 >...c/core-data/Common/MatDefs/Light</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Light/Lighting.frag?r=5108&amp;spec=svn5108"
 
 >...mmon/MatDefs/Light/Lighting.frag</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Light/Lighting.j3md?r=5108&amp;spec=svn5108"
 
 >...mmon/MatDefs/Light/Lighting.j3md</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Light/Lighting.vert?r=5108&amp;spec=svn5108"
 
 >...mmon/MatDefs/Light/Lighting.vert</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Light/LightingOld.frag?r=5108&amp;spec=svn5108"
 
 >...n/MatDefs/Light/LightingOld.frag</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Light/LightingOld.vert?r=5108&amp;spec=svn5108"
 
 >...n/MatDefs/Light/LightingOld.vert</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Light/Reflection.frag?r=5108&amp;spec=svn5108"
 
 >...on/MatDefs/Light/Reflection.frag</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Light/Reflection.j3md?r=5108&amp;spec=svn5108"
 
 >...on/MatDefs/Light/Reflection.j3md</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Light/Reflection.vert?r=5108&amp;spec=svn5108"
 
 >...on/MatDefs/Light/Reflection.vert</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc?r=5108&amp;spec=svn5108"
 
 >...rc/core-data/Common/MatDefs/Misc</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/Particle.frag?r=5108&amp;spec=svn5108"
 
 >...ommon/MatDefs/Misc/Particle.frag</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/Particle.j3md?r=5108&amp;spec=svn5108"
 
 >...ommon/MatDefs/Misc/Particle.j3md</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/Particle.vert?r=5108&amp;spec=svn5108"
 
 >...ommon/MatDefs/Misc/Particle.vert</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/ShowNormals.frag?r=5108&amp;spec=svn5108"
 
 >...on/MatDefs/Misc/ShowNormals.frag</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/ShowNormals.j3md?r=5108&amp;spec=svn5108"
 
 >...on/MatDefs/Misc/ShowNormals.j3md</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/ShowNormals.vert?r=5108&amp;spec=svn5108"
 
 >...on/MatDefs/Misc/ShowNormals.vert</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/SimpleTextured.frag?r=5108&amp;spec=svn5108"
 selected="selected"
 >...MatDefs/Misc/SimpleTextured.frag</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/SimpleTextured.j3md?r=5108&amp;spec=svn5108"
 
 >...MatDefs/Misc/SimpleTextured.j3md</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/SimpleTextured.vert?r=5108&amp;spec=svn5108"
 
 >...MatDefs/Misc/SimpleTextured.vert</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/Sky.frag?r=5108&amp;spec=svn5108"
 
 >...ata/Common/MatDefs/Misc/Sky.frag</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/Sky.j3md?r=5108&amp;spec=svn5108"
 
 >...ata/Common/MatDefs/Misc/Sky.j3md</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/Sky.vert?r=5108&amp;spec=svn5108"
 
 >...ata/Common/MatDefs/Misc/Sky.vert</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/SolidColor.frag?r=5108&amp;spec=svn5108"
 
 >...mon/MatDefs/Misc/SolidColor.frag</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/SolidColor.j3md?r=5108&amp;spec=svn5108"
 
 >...mon/MatDefs/Misc/SolidColor.j3md</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/SolidColor.vert?r=5108&amp;spec=svn5108"
 
 >...mon/MatDefs/Misc/SolidColor.vert</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/VertexColor.frag?r=5108&amp;spec=svn5108"
 
 >...on/MatDefs/Misc/VertexColor.frag</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/VertexColor.j3md?r=5108&amp;spec=svn5108"
 
 >...on/MatDefs/Misc/VertexColor.j3md</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/VertexColor.vert?r=5108&amp;spec=svn5108"
 
 >...on/MatDefs/Misc/VertexColor.vert</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Misc/WireColor.j3md?r=5108&amp;spec=svn5108"
 
 >...mmon/MatDefs/Misc/WireColor.j3md</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Shadow?r=5108&amp;spec=svn5108"
 
 >.../core-data/Common/MatDefs/Shadow</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Shadow/PostShadow.frag?r=5108&amp;spec=svn5108"
 
 >...n/MatDefs/Shadow/PostShadow.frag</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Shadow/PostShadow.j3md?r=5108&amp;spec=svn5108"
 
 >...n/MatDefs/Shadow/PostShadow.j3md</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Shadow/PostShadow.vert?r=5108&amp;spec=svn5108"
 
 >...n/MatDefs/Shadow/PostShadow.vert</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Shadow/PreShadow.frag?r=5108&amp;spec=svn5108"
 
 >...on/MatDefs/Shadow/PreShadow.frag</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Shadow/PreShadow.j3md?r=5108&amp;spec=svn5108"
 
 >...on/MatDefs/Shadow/PreShadow.j3md</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/MatDefs/Shadow/PreShadow.vert?r=5108&amp;spec=svn5108"
 
 >...on/MatDefs/Shadow/PreShadow.vert</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/Materials?r=5108&amp;spec=svn5108"
 
 >...3/src/core-data/Common/Materials</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/Materials/RedColor.j3m?r=5108&amp;spec=svn5108"
 
 >...ta/Common/Materials/RedColor.j3m</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/Materials/VertexColor.j3m?r=5108&amp;spec=svn5108"
 
 >...Common/Materials/VertexColor.j3m</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/Materials/WhiteColor.j3m?r=5108&amp;spec=svn5108"
 
 >.../Common/Materials/WhiteColor.j3m</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/ShaderLib?r=5108&amp;spec=svn5108"
 
 >...3/src/core-data/Common/ShaderLib</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/ShaderLib/Bump.glsllib?r=5108&amp;spec=svn5108"
 
 >...ta/Common/ShaderLib/Bump.glsllib</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/ShaderLib/Common.glsllib?r=5108&amp;spec=svn5108"
 
 >.../Common/ShaderLib/Common.glsllib</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/ShaderLib/Hdr.glsllib?r=5108&amp;spec=svn5108"
 
 >...ata/Common/ShaderLib/Hdr.glsllib</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/ShaderLib/Lighting.glsllib?r=5108&amp;spec=svn5108"
 
 >...ommon/ShaderLib/Lighting.glsllib</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/ShaderLib/Math.glsllib?r=5108&amp;spec=svn5108"
 
 >...ta/Common/ShaderLib/Math.glsllib</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/ShaderLib/Optics.glsllib?r=5108&amp;spec=svn5108"
 
 >.../Common/ShaderLib/Optics.glsllib</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/ShaderLib/Shadow.glsllib?r=5108&amp;spec=svn5108"
 
 >.../Common/ShaderLib/Shadow.glsllib</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/ShaderLib/Skinning.glsllib?r=5108&amp;spec=svn5108"
 
 >...ommon/ShaderLib/Skinning.glsllib</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/ShaderLib/Tangent.glsllib?r=5108&amp;spec=svn5108"
 
 >...Common/ShaderLib/Tangent.glsllib</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/ShaderLib/Texture.glsllib?r=5108&amp;spec=svn5108"
 
 >...Common/ShaderLib/Texture.glsllib</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Common/ShaderLib/Ubo.glsllib?r=5108&amp;spec=svn5108"
 
 >...ata/Common/ShaderLib/Ubo.glsllib</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Interface?r=5108&amp;spec=svn5108"
 
 >...hes/jme3/src/core-data/Interface</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Interface/Fonts?r=5108&amp;spec=svn5108"
 
 >...e3/src/core-data/Interface/Fonts</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Interface/Fonts/Console.fnt?r=5108&amp;spec=svn5108"
 
 >...data/Interface/Fonts/Console.fnt</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Interface/Fonts/Console.png?r=5108&amp;spec=svn5108"
 
 >...data/Interface/Fonts/Console.png</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Interface/Fonts/Default.fnt?r=5108&amp;spec=svn5108"
 
 >...data/Interface/Fonts/Default.fnt</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/Interface/Fonts/Default.png?r=5108&amp;spec=svn5108"
 
 >...data/Interface/Fonts/Default.png</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/fonts?r=5108&amp;spec=svn5108"
 
 >/branches/jme3/src/core-data/fonts</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/matdefs?r=5108&amp;spec=svn5108"
 
 >...nches/jme3/src/core-data/matdefs</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/shaderlib?r=5108&amp;spec=svn5108"
 
 >...hes/jme3/src/core-data/shaderlib</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/shaders?r=5108&amp;spec=svn5108"
 
 >...nches/jme3/src/core-data/shaders</option>
 
 <option value="/p/jmonkeyengine/source/browse/branches/jme3/src/core-data/textures?r=5108&amp;spec=svn5108"
 
 >...ches/jme3/src/core-data/textures</option>
 
 </select>
 </td></tr></table>
 
 
 



 
 </div>
 
 
 </div>
 <div class="round1"></div>
 <div class="round2"></div>
 <div class="round4"></div>
 </div>
 <div class="pmeta_bubble_bg" style="border:1px solid white">
 <div class="round4"></div>
 <div class="round2"></div>
 <div class="round1"></div>
 <div class="box-inner">
 <div id="older_bubble">
 <p>Older revisions</p>
 
 <a href="/p/jmonkeyengine/source/list?path=/branches/jme3/src/core-data/Common/MatDefs/Misc/SimpleTextured.frag&start=5108">All revisions of this file</a>
 </div>
 </div>
 <div class="round1"></div>
 <div class="round2"></div>
 <div class="round4"></div>
 </div>
 
 <div class="pmeta_bubble_bg" style="border:1px solid white">
 <div class="round4"></div>
 <div class="round2"></div>
 <div class="round1"></div>
 <div class="box-inner">
 <div id="fileinfo_bubble">
 <p>File info</p>
 
 <div>Size: 898 bytes,
 27 lines</div>
 
 <div><a href="//jmonkeyengine.googlecode.com/svn-history/r5108/branches/jme3/src/core-data/Common/MatDefs/Misc/SimpleTextured.frag">View raw file</a></div>
 </div>
 
 </div>
 <div class="round1"></div>
 <div class="round2"></div>
 <div class="round4"></div>
 </div>
 </div>
 </div>


</div>

</div>
</div>


<script src="http://www.gstatic.com/codesite/ph/9670661675484913303/js/source_file_scripts.js"></script>

 <script type="text/javascript" src="http://www.gstatic.com/codesite/ph/9670661675484913303/js/kibbles.js"></script>
 <script type="text/javascript">
 var lastStop = null;
 var initialized = false;
 
 function updateCursor(next, prev) {
 if (prev && prev.element) {
 prev.element.className = 'cursor_stop cursor_hidden';
 }
 if (next && next.element) {
 next.element.className = 'cursor_stop cursor';
 lastStop = next.index;
 }
 }
 
 function pubRevealed(data) {
 updateCursorForCell(data.cellId, 'cursor_stop cursor_hidden');
 if (initialized) {
 reloadCursors();
 }
 }
 
 function draftRevealed(data) {
 updateCursorForCell(data.cellId, 'cursor_stop cursor_hidden');
 if (initialized) {
 reloadCursors();
 }
 }
 
 function draftDestroyed(data) {
 updateCursorForCell(data.cellId, 'nocursor');
 if (initialized) {
 reloadCursors();
 }
 }
 function reloadCursors() {
 kibbles.skipper.reset();
 loadCursors();
 if (lastStop != null) {
 kibbles.skipper.setCurrentStop(lastStop);
 }
 }
 // possibly the simplest way to insert any newly added comments
 // is to update the class of the corresponding cursor row,
 // then refresh the entire list of rows.
 function updateCursorForCell(cellId, className) {
 var cell = document.getElementById(cellId);
 // we have to go two rows back to find the cursor location
 var row = getPreviousElement(cell.parentNode);
 row.className = className;
 }
 // returns the previous element, ignores text nodes.
 function getPreviousElement(e) {
 var element = e.previousSibling;
 if (element.nodeType == 3) {
 element = element.previousSibling;
 }
 if (element && element.tagName) {
 return element;
 }
 }
 function loadCursors() {
 // register our elements with skipper
 var elements = CR_getElements('*', 'cursor_stop');
 var len = elements.length;
 for (var i = 0; i < len; i++) {
 var element = elements[i]; 
 element.className = 'cursor_stop cursor_hidden';
 kibbles.skipper.append(element);
 }
 }
 function toggleComments() {
 CR_toggleCommentDisplay();
 reloadCursors();
 }
 function keysOnLoadHandler() {
 // setup skipper
 kibbles.skipper.addStopListener(
 kibbles.skipper.LISTENER_TYPE.PRE, updateCursor);
 // Set the 'offset' option to return the middle of the client area
 // an option can be a static value, or a callback
 kibbles.skipper.setOption('padding_top', 50);
 // Set the 'offset' option to return the middle of the client area
 // an option can be a static value, or a callback
 kibbles.skipper.setOption('padding_bottom', 100);
 // Register our keys
 kibbles.skipper.addFwdKey("n");
 kibbles.skipper.addRevKey("p");
 kibbles.keys.addKeyPressListener(
 'u', function() { window.location = detail_url; });
 kibbles.keys.addKeyPressListener(
 'r', function() { window.location = detail_url + '#publish'; });
 
 kibbles.keys.addKeyPressListener('j', gotoNextPage);
 kibbles.keys.addKeyPressListener('k', gotoPreviousPage);
 
 
 }
 </script>
<script src="http://www.gstatic.com/codesite/ph/9670661675484913303/js/code_review_scripts.js"></script>
<script type="text/javascript">
 function showPublishInstructions() {
 var element = document.getElementById('review_instr');
 if (element) {
 element.className = 'opened';
 }
 }
 var codereviews;
 function revsOnLoadHandler() {
 // register our source container with the commenting code
 var paths = {'svn5108': '/branches/jme3/src/core-data/Common/MatDefs/Misc/SimpleTextured.frag'}
 codereviews = CR_controller.setup(
 {"domainName":null,"relativeBaseUrl":"","loggedInUserEmail":"akis100@gmail.com","assetHostPath":"http://www.gstatic.com/codesite/ph","assetVersionPath":"http://www.gstatic.com/codesite/ph/9670661675484913303","projectHomeUrl":"/p/jmonkeyengine","profileUrl":"/u/116090281484404608661/","projectName":"jmonkeyengine","token":"nZW5oObCK9ko18gW1fBg8d_BkyA:1382005184567"}, '', 'svn5108', paths,
 CR_BrowseIntegrationFactory);
 
 codereviews.registerActivityListener(CR_ActivityType.REVEAL_DRAFT_PLATE, showPublishInstructions);
 
 codereviews.registerActivityListener(CR_ActivityType.REVEAL_PUB_PLATE, pubRevealed);
 codereviews.registerActivityListener(CR_ActivityType.REVEAL_DRAFT_PLATE, draftRevealed);
 codereviews.registerActivityListener(CR_ActivityType.DISCARD_DRAFT_COMMENT, draftDestroyed);
 
 
 
 
 
 
 
 var initialized = true;
 reloadCursors();
 }
 window.onload = function() {keysOnLoadHandler(); revsOnLoadHandler();};

</script>
<script type="text/javascript" src="http://www.gstatic.com/codesite/ph/9670661675484913303/js/dit_scripts.js"></script>

 
 
 
 <script type="text/javascript" src="http://www.gstatic.com/codesite/ph/9670661675484913303/js/ph_core.js"></script>
 
 
 
 
</div> 

<div id="footer" dir="ltr">
 <div class="text">
 <a href="/projecthosting/terms.html">Terms</a> -
 <a href="http://www.google.com/privacy.html">Privacy</a> -
 <a href="/p/support/">Project Hosting Help</a>
 </div>
</div>
 <div class="hostedBy" style="margin-top: -20px;">
 <span style="vertical-align: top;">Powered by <a href="http://code.google.com/projecthosting/">Google Project Hosting</a></span>
 </div>

 
 


 
 </body>
</html>

