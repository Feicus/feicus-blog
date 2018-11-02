/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/

define(function(require, exports, module) {

    var initEditor = function (callback) {
        require.async(['form', 'tinymce'], function () {
            var options = {
                selector: "#content",
                theme: 'modern',
                upload_image_url: window.app.base + "/post/upload", //配置的上传图片的路由
                height: 400,
                plugins: [
                    "advlist autolink autosave link image lists charmap print preview hr anchor pagebreak spellchecker",
                    "searchreplace wordcount visualblocks visualchars code fullscreen insertdatetime media nonbreaking",
                    "table contextmenu directionality emoticons template textcolor paste fullpage textcolor wordcount"
                ],
                block_formats: "Paragraph=p;Heading 1=h1;Heading 2=h2;Heading 3=h3;Heading 4=h4;Heading 5=h5;Heading 6=h6;Preformatted=pre",
                toolbar1: "undo redo | cut copy paste | bold italic underline strikethrough | alignleft aligncenter alignright alignjustify |",
                toolbar2: " searchreplace | bullist numlist | outdent indent blockquote | link unlink anchor code | inserttime preview | forecolor backcolor|",
                toolbar3: "table | hr removeformat | subscript superscript | charmap emoticons | print  | ltr rtl | spellchecker | visualchars visualblocks nonbreaking template pagebreak restoredraft",
                toolbar4 : "styleselect formatselect fontselect fontsizeselect | image fullpage | fullscreen |",
                style_formats: [
                    {title: 'Bold text', inline: 'b'},
                    {title: 'Red text', inline: 'span', styles: {color: '#ff0000'}},
                    {title: 'Red header', block: 'h1', styles: {color: '#ff0000'}},
                    {title: 'Example 1', inline: 'span', classes: 'example1'},
                    {title: 'Example 2', inline: 'span', classes: 'example2'},
                    {title: 'Table styles'},
                    {title: 'Table row 1', selector: 'tr', classes: 'tablerow1'}
                ],
                images_upload_handler: function(blobInfo, success, failure){
                    var xhr, formData;
                    xhr = new XMLHttpRequest();
                    xhr.withCredentials = false;
                    xhr.open("POST", window.app.base + "/post/uploadImage");
                    formData = new FormData();
                    formData.append("file", blobInfo.blob());
                    xhr.onload = function(e){
                        var json;

                        if (xhr.status != 200) {
                            failure('HTTP Error: ' + xhr.status);
                            return;
                        }
                        json = JSON.parse(this.responseText);

                        if (!json || typeof json.location != 'string') {
                            failure('Invalid JSON: ' + xhr.responseText);
                            return;
                        }
                        success(json.location);
                        failure(json.errorInfo);
                    };
                    xhr.send(formData);
                },
                templates: [
                    {title: 'Test template 1', content: 'Test 1'},
                    {title: 'Test template 2', content: 'Test 2'}
                ],
                menubar: "file edit insert view format table",
                menu: {
                    file: {title: 'File', items: 'newdocument'},
                    edit: {title: 'Edit', items: 'undo redo | cut copy paste pastetext | selectall'},
                    insert: {title: 'Insert', items: 'link image media | template hr'},
                    view: {title: 'View', items: 'visualaid'},
                    format: {title: 'Format', items: 'bold italic underline strikethrough superscript subscript | formats | removeformat'},
                    table: {title: 'Table', items: 'inserttable tableprops deletetable | cell row column'},
                },
                font_formats: "宋体=宋体;黑体=黑体;仿宋=仿宋;楷体=楷体;隶书=隶书;幼圆=幼圆;Arial=arial,helvetica,sans-serif;Comic Sans MS=comic sans ms,sans-serif;Courier New=courier new,courier;Tahoma=tahoma,arial,helvetica,sans-serif;Times New Roman=times new roman,times;Verdana=verdana,geneva;Webdings=webdings;Wingdings=wingdings,zapf dingbats",
                convert_urls: false,
                language: "zh_CN",
                statusbar : false,
                body_class: 'markdown-body',
                codesample_dialog_width: '600',
                codesample_dialog_height: '400',
                paste_data_images: true,
                content_css: [
                    window.app.base + '/dist/vendors/bootstrap/css/bootstrap.min.css',
                    window.app.base + '/theme/default/css/editor.css',
                ]
                //参考网站 https://www.tinymce.com/
            };

            if ($(window).width() < 900) {
                options.theme = 'mobile';
                options.toolbar = "undo redo | bold underline blockquote | alignleft aligncenter alignright | uploadimage"
            }
            tinymce.init(options);

            callback.call(this);
        });

    };

	exports.init = function (callback) {
        initEditor(callback);
    };
});