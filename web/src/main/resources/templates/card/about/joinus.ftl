<#include "/card/utils/ui.ftl"/>

<@layout "加入我们">

<div class="row main">
    <div class="col-xs-12 col-md-9 side-left topics-show">
        <!-- view show -->
        <div class="topic panel panel-default">
            <div class="infos panel-heading">

                <h1 class="panel-title topic-title"></h1>

                <div class="meta inline-block">
                    <a class="author" href="#"></a>
                </div>
                <div class="clearfix"></div>
            </div>

            <div class="content-body entry-content panel-body ">
                <div class="markdown-body" id="emojify">
                </div>
            </div>
            <div class="panel-footer operate">
            </div>
            <div class="panel-footer operate">
                <div class="hidden-xs">
                    <div class="social-share" data-sites="weibo, wechat, facebook, twitter, google, qzone, qq"></div>
                </div>
                <div class="clearfix"></div>
            </div>
        </div>
        <!-- /view show -->
    </div>
    <div class="col-xs-12 col-md-3 side-right hidden-xs hidden-sm">
		<#include "/card/inc/right.ftl"/>
    </div>
</div>

</@layout>