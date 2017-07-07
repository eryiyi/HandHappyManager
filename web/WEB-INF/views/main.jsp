<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!--Start Content-->
<div class="row">
    <div id="breadcrumb" class="col-xs-12">
        <a href="#" class="show-sidebar">
            <i class="fa fa-bars"></i>
        </a>
        <ol class="breadcrumb pull-left">
            <li><a href="javascript:void(0)" onclick="toPage('mainPage','')">主页</a></li>
        </ol>
    </div>
</div>
<div class="cat-content">

    <a href="javascript:void(0);"></a>

    <div class="row">

        <div class="col-md-4 col-sm-6">
            <a href="http://www.qianshoulove.com" class="cat-item bg-red" target="_blank">
                <span>幸福牵手吧网页</span>
            </a>
        </div>
        <div class="col-md-4 col-sm-6">
            <a href="http://android.myapp.com/myapp/detail.htm?apkName=com.lbins.meetlove" class="cat-item bg-green" target="_blank">
                <span>幸福牵手吧下载</span>
            </a>
        </div>
    </div>

    <div class="row">
        <h3 class="col-md-12 cat-title">会员统计</h3>
    </div>

    <div class="row">
        <div class="col-md-4 col-sm-6">
            <a href="javascript:void(0);" onclick="toPage('/emp/list','1')" class="cat-item bg-blue">
                <span>注册用户总人数</span>
                <i>${empCountAll}</i>
                <%--<input type="button" class="btn-export" value="导出">--%>
            </a>
        </div>
        <div class="col-md-4 col-sm-6">
            <a href="javascript:void(0);" onclick="toPage('/emp/list','1')" class="cat-item bg-blue">
                <span>今日注册用户数</span>
                <i>${empCountDay}</i>
                <%--<input type="button" class="btn-export" value="导出">--%>
            </a>
        </div>
    </div>


    <div class="row">
        <h3 class="col-md-12 cat-title">认证会员统计</h3>
    </div>

    <div class="row">
        <div class="col-md-4 col-sm-6">
            <a href="javascript:void(0);" onclick="toPage('/emp/list','1')" class="cat-item bg-blue">
                <span>认证会员总数</span>
                <i>${empCountAllRz}</i>
                <%--<input type="button" class="btn-export" value="导出">--%>
            </a>
        </div>
        <div class="col-md-4 col-sm-6">
            <a href="javascript:void(0);" onclick="toPage('/emp/list','1')" class="cat-item bg-blue">
                <span>今日认证会员数</span>
                <i>${empCountDayRz}</i>
                <%--<input type="button" class="btn-export" value="导出">--%>
            </a>
        </div>
    </div>


    <div class="row">
        <h3 class="col-md-12 cat-title">体验/诚信会员统计</h3>
    </div>

    <div class="row">
        <div class="col-md-4 col-sm-6">
            <a href="javascript:void(0);" onclick="toPage('/emp/list','1')" class="cat-item bg-blue">
                <span>体验会员总数</span>
                <i>${empCountAllTy}</i>
                <%--<input type="button" class="btn-export" value="导出">--%>
            </a>
        </div>

        <div class="col-md-4 col-sm-6">
            <a href="javascript:void(0);" onclick="toPage('/emp/list','1')" class="cat-item bg-blue">
                <span>诚信会员总数</span>
                <i>${empCountAllCx}</i>
                <%--<input type="button" class="btn-export" value="导出">--%>
            </a>
        </div>
    </div>


    <div class="row">
        <h3 class="col-md-12 cat-title">在线人数统计</h3>
    </div>
    <div class="row">
        <div class="col-md-4 col-sm-6">
            <a href="javascript:void(0);" class="cat-item bg-green">
                <span>在线人数总数</span>
                <i>${lon1}</i>
                <%--<input type="button" class="btn-export" value="导出">--%>
            </a>
        </div>
        <div class="col-md-4 col-sm-6">
            <a href="javascript:void(0);"  class="cat-item bg-green">
                <span>昨日登录会员</span>
                <i>${lon11}</i>
                <%--<input type="button" class="btn-export" value="导出">--%>
            </a>
        </div>
    </div>


    <div class="row">
        <h3 class="col-md-12 cat-title">收入统计</h3>
    </div>
    <div class="row">
        <div class="col-md-4 col-sm-6">
            <a href="javascript:void(0);" onclick="toPage('orders/list','1')" class="cat-item bg-red">
                <span>收入总额</span>
                <i>${orderAmountAll}</i>
                <%--<input type="button" class="btn-export" value="导出">--%>
            </a>
        </div>
        <div class="col-md-4 col-sm-6">
            <a href="javascript:void(0);" onclick="toPage('orders/list','1')" class="cat-item bg-red">
                <span>今日总额</span>
                <i>${orderAmountDay}</i>
                <%--<input type="button" class="btn-export" value="导出">--%>
            </a>
        </div>
    </div>
    <div class="row">

        <div class="col-md-4 col-sm-6">
            <a href="javascript:void(0);" onclick="toPage('orders/list','1')" class="cat-item bg-red">
                <span>会员费总额</span>
                <i>${orderAmountAllHy}</i>
                <%--<input type="button" class="btn-export" value="导出">--%>
            </a>
        </div>

        <div class="col-md-4 col-sm-6">
            <a href="javascript:void(0);" onclick="toPage('orders/list','1')" class="cat-item bg-red">
                <span>诚信费总额</span>
                <i>${orderAmountAllCx}</i>
                <%--<input type="button" class="btn-export" value="导出">--%>
            </a>
        </div>

    </div>
</div>
<!--End Content-->
<script type="text/javascript">
    function toPage(_url, _page) {
        if (_page != '') {
            window.location.href = "#module=" + _url + "&page=" + _page + "&_t=" + new Date().getTime();
        } else {
            window.location.href = "#module=" + _url + "&_t=" + new Date().getTime();
        }
    }

</script>