<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no" />
    <!--[if lt IE 9]>
    <script type="text/javascript" src="/resources/js/html5shiv.js"></script>
    <![endif]-->
    <style>
        body, dl, dt, dd, ul, ol, li, h1, h2, h3, h4, h5, h6, form, fieldset, input, button, p , figure {
            margin:0;
            padding:0;
        }
        article, nav, section, aside, header, footer, hgroup, figure {
            display:block;
        }
        ol, ul {
            list-style:none;
        }
        h1, h2, h3, h4, h5, h6 {
            font-size:100%;
            font-weight:normal;
        }
        .btUserBtn,.btUserBtn:hover{font-style:normal;font-weight:normal;}
        .clr:after { visibility: hidden;display: block;font-size: 0;content: "";clear: both; height: 0; }
        .clr {*zoom:1;}
        .clear {clear: both;width: 0;height: 0;font-size: 0;line-height: 0;margin: 0;padding: 0;overflow: hidden;}
        .warp {
            width: 980px;
            margin: 0 auto;
        }
        .loca {
            padding: 30px 0 20px 0;
        }
        .loca li {
            float: left;
            position: relative;
            color: #8e8f8f;
            padding-left: 15px;
            padding-right: 9px;
        }
        .loca li a {
            color: #8e8f8f;
        }
        .m_left {
            float: left;
            width: 720px;
            margin-bottom: 50px;
            position: relative;
        }

        .newsinfo .title h3 {
            font-size: 30px;
            color: #201f1f;
            text-align: center;
            padding: 10px 0 20px 0;
            font-weight: bold;
            word-break:break-all;
            word-wrap:break-word;
            text-align: center;
        }
        .newsinfo .source {
            color: #646565;
            text-align: center;
            margin-bottom: 30px;
        }
        .newsinfo .source li {
            display: inline;
            margin: 0 25px;
            font-size:12px
        }
        .newsinfo .a_content {
            font-size: 14px;
        }
        .newsinfo .a_content, .newsinfo .a_content p {
            color: #3a3b3b;
            line-height: 2em;
            text-indent: 2em;
            word-break: break-all;
            word-wrap: break-word;
            margin-bottom:20px;
        }
        .newsinfo .a_content,.newsinfo .a_content p img{
            max-width:704px;
        }
        .newsinfo .a_content p {
            margin-bottom: 1.5em;
        }

    </style>

</head>

<body id='cloudReader'>
<div class="main warp">
    <div class="m_left">
        <div class="newsinfo">
        	 <div id="JS_aContent" class="a_content" data-layer="true">
           		${content} 
           	</div>
        </div>
    </div>
</div>
</body>
</html>
