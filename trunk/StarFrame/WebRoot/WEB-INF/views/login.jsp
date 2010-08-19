<%@page contentType="text/html;charset=UTF-8"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <link rel="stylesheet" type="text/css" href="./resources/scripts/ext-3.2.1/resources/css/ext-all.css" />
    <script type="text/javascript" src="./resources/scripts/ext-3.2.1/ext-base.js"></script>
    <script type="text/javascript" src="./resources/scripts/ext-3.2.1/ext-all.js"></script>
    <script type="text/javascript" src="./resources/scripts/ext-3.2.1/ext-lang-zh_CN.js"></script>

	<script type="text/javascript" src="./resources/scripts/core/ST.Core.Login.js"></script>
	<script type="text/javascript"> 
	     if(top!=self){
	          if(top.location != self.location)
	               top.location=self.location; 
	     }
	</script>
	<style type="text/css">
	    body {
			background:#3d71b8 url(./resources/images/core/login/desktop.jpg) no-repeat left top;
		    font: normal 12px tahoma, arial, verdana, sans-serif;
			margin: 0;
			padding: 0;
			border: 0 none;
			overflow: hidden;
			height: 100%;
		}
	</style>
  </head>
  <body>
	<div>
		<div id="login"></div>
	</div>
	<div style="display: none">
		AJAX-AccessDeniedException
	</div>
  </body>
</html>