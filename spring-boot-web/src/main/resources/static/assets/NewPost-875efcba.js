import{aO as h,aK as g,aG as b,aF as a,bd as f}from"./SuspenseLoading-261fce6d.js";import{u as o,a as y,B as v,G as w}from"./main-cba63024.js";import{T as C,g as I}from"./PostTags-f148c3f3.js";import{F as t}from"./index-8728d8fc.js";import{I as F}from"./index-01b69c7c.js";import{B as T}from"./RouteError-62a31d50.js";const P=[{label:"History",value:"history"},{label:"Fiction",value:"fiction"},{label:"Crime",value:"crime"},{label:"Magical",value:"magical"},{label:"Mystery",value:"mystery"},{label:"Love",value:"love"},{label:"Classic",value:"classic"}];function M(){h("新增文章");const r=o(e=>e.ui.loading),i=o(e=>e.auth.userId),n=y(),u=g();function m(e){const l={userId:i,tags:e.tags,title:e.title.trim(),body:e.body.trim()};n(w(l,()=>u("/posts")))}return b(t,{name:"new-post",labelCol:{span:4},wrapperCol:{span:16},onFinish:m,autoComplete:"off",children:[a(t.Item,{label:"标签",name:"tags",rules:[{required:!0,message:"文章标签不能为空"}],children:a(v,{mode:"multiple",allowClear:!0,style:{width:"100%"},placeholder:"请选择文章所对应的标签",options:P,tagRender:e=>{const{label:l,value:p,closable:c,onClose:d}=e;return a(C,{color:I(p),onMouseDown:s=>{s.preventDefault(),s.stopPropagation()},closable:c,onClose:d,style:{marginRight:3},children:l})}})}),a(t.Item,{label:"文章标题",name:"title",rules:[{required:!0,whitespace:!0,message:"文章标题不能为空"}],children:a(F,{placeholder:"请输入文章标题"})}),a(t.Item,{label:"文章内容",name:"body",rules:[{required:!0,whitespace:!0,message:"文章内容不能为空"}],children:a(f,{placeholder:"请输入文章内容",rows:4})}),a(t.Item,{wrapperCol:{offset:4},children:a(T,{type:"primary",loading:r,htmlType:"submit",children:"新增"})})]})}export{P as TAGS,M as default};
