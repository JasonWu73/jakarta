import{r as o,i as Ve,m as He,be as Ne,j as Pe,bf as ue,ao as Ce,H as D,l as qe,k as Q,bg as Ae,bh as De,a0 as Be,a3 as Xe,C as de,bi as Ge,bj as Ke,bk as J,bl as Qe,a2 as fe,w as xe,z as Ye,A as Ue,_ as $e,W as Je,an as Ze,b4 as ke,ab as et,x as tt,Q as nt,bm as je,o as rt,bn as ot,bo as lt,ac as it,bp as at,am as st,ag as ct,al as mt,bq as ut,br as dt,bs as ft}from"./SuspenseLoading-261fce6d.js";import{k as gt,I as Me,R as pt,J as ht,E as bt}from"./main-cba63024.js";import{L as yt}from"./RouteError-62a31d50.js";let ve=e=>typeof e=="object"&&e!=null&&e.nodeType===1,we=(e,t)=>(!t||e!=="hidden")&&e!=="visible"&&e!=="clip",ce=(e,t)=>{if(e.clientHeight<e.scrollHeight||e.clientWidth<e.scrollWidth){let r=getComputedStyle(e,null);return we(r.overflowY,t)||we(r.overflowX,t)||(n=>{let l=(a=>{if(!a.ownerDocument||!a.ownerDocument.defaultView)return null;try{return a.ownerDocument.defaultView.frameElement}catch{return null}})(n);return!!l&&(l.clientHeight<n.scrollHeight||l.clientWidth<n.scrollWidth)})(e)}return!1},oe=(e,t,r,n,l,a,s,c)=>a<e&&s>t||a>e&&s<t?0:a<=e&&c<=r||s>=t&&c>=r?a-e-n:s>t&&c<r||a<e&&c>r?s-t+l:0,Ct=e=>{let t=e.parentElement;return t??(e.getRootNode().host||null)},Se=(e,t)=>{var r,n,l,a;if(typeof document>"u")return[];let{scrollMode:s,block:c,inline:i,boundary:f,skipOverflowHiddenElements:g}=t,j=typeof f=="function"?f:q=>q!==f;if(!ve(e))throw new TypeError("Invalid target");let T=document.scrollingElement||document.documentElement,C=[],p=e;for(;ve(p)&&j(p);){if(p=Ct(p),p===T){C.push(p);break}p!=null&&p===document.body&&ce(p)&&!ce(document.documentElement)||p!=null&&ce(p,g)&&C.push(p)}let N=(n=(r=window.visualViewport)==null?void 0:r.width)!=null?n:innerWidth,x=(a=(l=window.visualViewport)==null?void 0:l.height)!=null?a:innerHeight,{scrollX:m,scrollY:E}=window,{height:b,width:$,top:d,right:v,bottom:I,left:L}=e.getBoundingClientRect(),y=c==="start"||c==="nearest"?d:c==="end"?I:d+b/2,w=i==="center"?L+$/2:i==="end"?v:L,R=[];for(let q=0;q<C.length;q++){let u=C[q],{height:B,width:z,top:P,right:O,bottom:A,left:X}=u.getBoundingClientRect();if(s==="if-needed"&&d>=0&&L>=0&&I<=x&&v<=N&&d>=P&&I<=A&&L>=X&&v<=O)return R;let V=getComputedStyle(u),H=parseInt(V.borderLeftWidth,10),h=parseInt(V.borderTopWidth,10),M=parseInt(V.borderRightWidth,10),F=parseInt(V.borderBottomWidth,10),_=0,W=0,G="offsetWidth"in u?u.offsetWidth-u.clientWidth-H-M:0,K="offsetHeight"in u?u.offsetHeight-u.clientHeight-h-F:0,U="offsetWidth"in u?u.offsetWidth===0?0:z/u.offsetWidth:0,S="offsetHeight"in u?u.offsetHeight===0?0:B/u.offsetHeight:0;if(T===u)_=c==="start"?y:c==="end"?y-x:c==="nearest"?oe(E,E+x,x,h,F,E+y,E+y+b,b):y-x/2,W=i==="start"?w:i==="center"?w-N/2:i==="end"?w-N:oe(m,m+N,N,H,M,m+w,m+w+$,$),_=Math.max(0,_+E),W=Math.max(0,W+m);else{_=c==="start"?y-P-h:c==="end"?y-A+F+K:c==="nearest"?oe(P,A,B,h,F+K,y,y+b,b):y-(P+B/2)+K/2,W=i==="start"?w-X-H:i==="center"?w-(X+z/2)+G/2:i==="end"?w-O+M+G:oe(X,O,z,H,M+G,w,w+$,$);let{scrollLeft:Z,scrollTop:ne}=u;_=Math.max(0,Math.min(ne+_/S,u.scrollHeight-B/S+K)),W=Math.max(0,Math.min(Z+W/U,u.scrollWidth-z/U+G)),y+=ne-_,w+=Z-W}R.push({el:u,top:_,left:W})}return R},xt=e=>typeof e=="object"&&typeof e.behavior=="function",$t=e=>e===!1?{block:"end",inline:"nearest"}:(t=>t===Object(t)&&Object.keys(t).length!==0)(e)?e:{block:"start",inline:"nearest"};const vt=e=>{for(var t=e;t&&t.parentNode;){if(t.parentNode===document)return!0;t=t.parentNode instanceof ShadowRoot?t.parentNode.host:t.parentNode}return!1};function wt(e,t){if(!e.isConnected||!vt(e))return;if(xt(t))return t.behavior(Se(e,t));let r=typeof t=="boolean"||t==null?void 0:t.behavior;for(let{el:n,top:l,left:a}of Se(e,$t(t)))n.scroll({top:l,left:a,behavior:r})}function le(e){const[t,r]=o.useState(e);return o.useEffect(()=>{const n=setTimeout(()=>{r(e)},e.length?0:10);return()=>{clearTimeout(n)}},[e]),t}const St=e=>{const{componentCls:t}=e,r=`${t}-show-help`,n=`${t}-show-help-item`;return{[r]:{transition:`opacity ${e.motionDurationSlow} ${e.motionEaseInOut}`,"&-appear, &-enter":{opacity:0,"&-active":{opacity:1}},"&-leave":{opacity:1,"&-active":{opacity:0}},[n]:{overflow:"hidden",transition:`height ${e.motionDurationSlow} ${e.motionEaseInOut},
                     opacity ${e.motionDurationSlow} ${e.motionEaseInOut},
                     transform ${e.motionDurationSlow} ${e.motionEaseInOut} !important`,[`&${n}-appear, &${n}-enter`]:{transform:"translateY(-5px)",opacity:0,["&-active"]:{transform:"translateY(0)",opacity:1}},[`&${n}-leave-active`]:{transform:"translateY(-5px)"}}}}},Et=St,It=e=>({legend:{display:"block",width:"100%",marginBottom:e.marginLG,padding:0,color:e.colorTextDescription,fontSize:e.fontSizeLG,lineHeight:"inherit",border:0,borderBottom:`${e.lineWidth}px ${e.lineType} ${e.colorBorder}`},label:{fontSize:e.fontSize},'input[type="search"]':{boxSizing:"border-box"},'input[type="radio"], input[type="checkbox"]':{lineHeight:"normal"},'input[type="file"]':{display:"block"},'input[type="range"]':{display:"block",width:"100%"},"select[multiple], select[size]":{height:"auto"},[`input[type='file']:focus,
  input[type='radio']:focus,
  input[type='checkbox']:focus`]:{outline:0,boxShadow:`0 0 0 ${e.controlOutlineWidth}px ${e.controlOutline}`},output:{display:"block",paddingTop:15,color:e.colorText,fontSize:e.fontSize,lineHeight:e.lineHeight}}),Ee=(e,t)=>{const{formItemCls:r}=e;return{[r]:{[`${r}-label > label`]:{height:t},[`${r}-control-input`]:{minHeight:t}}}},Ot=e=>{const{componentCls:t}=e;return{[e.componentCls]:Object.assign(Object.assign(Object.assign({},Pe(e)),It(e)),{[`${t}-text`]:{display:"inline-block",paddingInlineEnd:e.paddingSM},"&-small":Object.assign({},Ee(e,e.controlHeightSM)),"&-large":Object.assign({},Ee(e,e.controlHeightLG))})}},Ft=e=>{const{formItemCls:t,iconCls:r,componentCls:n,rootPrefixCls:l}=e;return{[t]:Object.assign(Object.assign({},Pe(e)),{marginBottom:e.marginLG,verticalAlign:"top","&-with-help":{transition:"none"},[`&-hidden,
        &-hidden.${l}-row`]:{display:"none"},"&-has-warning":{[`${t}-split`]:{color:e.colorError}},"&-has-error":{[`${t}-split`]:{color:e.colorWarning}},[`${t}-label`]:{display:"inline-block",flexGrow:0,overflow:"hidden",whiteSpace:"nowrap",textAlign:"end",verticalAlign:"middle","&-left":{textAlign:"start"},"&-wrap":{overflow:"unset",lineHeight:`${e.lineHeight} - 0.25em`,whiteSpace:"unset"},"> label":{position:"relative",display:"inline-flex",alignItems:"center",maxWidth:"100%",height:e.controlHeight,color:e.colorTextHeading,fontSize:e.fontSize,[`> ${r}`]:{fontSize:e.fontSize,verticalAlign:"top"},[`&${t}-required:not(${t}-required-mark-optional)::before`]:{display:"inline-block",marginInlineEnd:e.marginXXS,color:e.colorError,fontSize:e.fontSize,fontFamily:"SimSun, sans-serif",lineHeight:1,content:'"*"',[`${n}-hide-required-mark &`]:{display:"none"}},[`${t}-optional`]:{display:"inline-block",marginInlineStart:e.marginXXS,color:e.colorTextDescription,[`${n}-hide-required-mark &`]:{display:"none"}},[`${t}-tooltip`]:{color:e.colorTextDescription,cursor:"help",writingMode:"horizontal-tb",marginInlineStart:e.marginXXS},"&::after":{content:'":"',position:"relative",marginBlock:0,marginInlineStart:e.marginXXS/2,marginInlineEnd:e.marginXS},[`&${t}-no-colon::after`]:{content:'" "'}}},[`${t}-control`]:{display:"flex",flexDirection:"column",flexGrow:1,[`&:first-child:not([class^="'${l}-col-'"]):not([class*="' ${l}-col-'"])`]:{width:"100%"},"&-input":{position:"relative",display:"flex",alignItems:"center",minHeight:e.controlHeight,"&-content":{flex:"auto",maxWidth:"100%"}}},[t]:{"&-explain, &-extra":{clear:"both",color:e.colorTextDescription,fontSize:e.fontSize,lineHeight:e.lineHeight},"&-explain-connected":{width:"100%"},"&-extra":{minHeight:e.controlHeightSM,transition:`color ${e.motionDurationMid} ${e.motionEaseOut}`},"&-explain":{"&-error":{color:e.colorError},"&-warning":{color:e.colorWarning}}},[`&-with-help ${t}-explain`]:{height:"auto",opacity:1},[`${t}-feedback-icon`]:{fontSize:e.fontSize,textAlign:"center",visibility:"visible",animationName:Ne,animationDuration:e.motionDurationMid,animationTimingFunction:e.motionEaseOutBack,pointerEvents:"none","&-success":{color:e.colorSuccess},"&-error":{color:e.colorError},"&-warning":{color:e.colorWarning},"&-validating":{color:e.colorPrimary}}})}},Nt=e=>{const{componentCls:t,formItemCls:r,rootPrefixCls:n}=e;return{[`${t}-horizontal`]:{[`${r}-label`]:{flexGrow:0},[`${r}-control`]:{flex:"1 1 0",minWidth:0},[`${r}-label.${n}-col-24 + ${r}-control`]:{minWidth:"unset"}}}},Pt=e=>{const{componentCls:t,formItemCls:r}=e;return{[`${t}-inline`]:{display:"flex",flexWrap:"wrap",[r]:{flex:"none",marginInlineEnd:e.margin,marginBottom:0,"&-row":{flexWrap:"nowrap"},"&-with-help":{marginBottom:e.marginLG},[`> ${r}-label,
        > ${r}-control`]:{display:"inline-block",verticalAlign:"top"},[`> ${r}-label`]:{flex:"none"},[`${t}-text`]:{display:"inline-block"},[`${r}-has-feedback`]:{display:"inline-block"}}}}},ee=e=>({margin:0,padding:`0 0 ${e.paddingXS}px`,whiteSpace:"initial",textAlign:"start","> label":{margin:0,"&::after":{display:"none"}}}),jt=e=>{const{componentCls:t,formItemCls:r}=e;return{[`${r} ${r}-label`]:ee(e),[t]:{[r]:{flexWrap:"wrap",[`${r}-label,
          ${r}-control`]:{flex:"0 0 100%",maxWidth:"100%"}}}}},Mt=e=>{const{componentCls:t,formItemCls:r,rootPrefixCls:n}=e;return{[`${t}-vertical`]:{[r]:{"&-row":{flexDirection:"column"},"&-label > label":{height:"auto"},[`${t}-item-control`]:{width:"100%"}}},[`${t}-vertical ${r}-label,
      .${n}-col-24${r}-label,
      .${n}-col-xl-24${r}-label`]:ee(e),[`@media (max-width: ${e.screenXSMax}px)`]:[jt(e),{[t]:{[`.${n}-col-xs-24${r}-label`]:ee(e)}}],[`@media (max-width: ${e.screenSMMax}px)`]:{[t]:{[`.${n}-col-sm-24${r}-label`]:ee(e)}},[`@media (max-width: ${e.screenMDMax}px)`]:{[t]:{[`.${n}-col-md-24${r}-label`]:ee(e)}},[`@media (max-width: ${e.screenLGMax}px)`]:{[t]:{[`.${n}-col-lg-24${r}-label`]:ee(e)}}}},ge=Ve("Form",(e,t)=>{let{rootPrefixCls:r}=t;const n=He(e,{formItemCls:`${e.componentCls}-item`,rootPrefixCls:r});return[Ot(n),Ft(n),Et(n),Nt(n),Pt(n),Mt(n),gt(n),Ne]}),Ie=[];function me(e,t,r){let n=arguments.length>3&&arguments[3]!==void 0?arguments[3]:0;return{key:typeof e=="string"?e:`${t}-${n}`,error:e,errorStatus:r}}function Re(e){let{help:t,helpStatus:r,errors:n=Ie,warnings:l=Ie,className:a,fieldId:s,onVisibleChanged:c}=e;const{prefixCls:i}=o.useContext(ue),f=`${i}-item-explain`,[,g]=ge(i),j=o.useMemo(()=>Ce(i),[i]),T=le(n),C=le(l),p=o.useMemo(()=>t!=null?[me(t,"help",r)]:[].concat(D(T.map((x,m)=>me(x,"error","error",m))),D(C.map((x,m)=>me(x,"warning","warning",m)))),[t,r,T,C]),N={};return s&&(N.id=`${s}_help`),o.createElement(qe,{motionDeadline:j.motionDeadline,motionName:`${i}-show-help`,visible:!!p.length,onVisibleChanged:c},x=>{const{className:m,style:E}=x;return o.createElement("div",Object.assign({},N,{className:Q(f,m,a,g),style:E,role:"alert"}),o.createElement(Ae,Object.assign({keys:p},Ce(i),{motionName:`${i}-show-help-item`,component:!1}),b=>{const{key:$,error:d,errorStatus:v,className:I,style:L}=b;return o.createElement("div",{key:$,className:Q(I,{[`${f}-${v}`]:v}),style:L},d)}))})}const Rt=["parentNode"],_t="form_item";function te(e){return e===void 0||e===!1?[]:Array.isArray(e)?e:[e]}function _e(e,t){if(!e.length)return;const r=e.join("_");return t?`${t}_${r}`:Rt.includes(r)?`${_t}_${r}`:r}function Oe(e){return te(e).join("_")}function Te(e){const[t]=De(),r=o.useRef({}),n=o.useMemo(()=>e??Object.assign(Object.assign({},t),{__INTERNAL__:{itemRef:l=>a=>{const s=Oe(l);a?r.current[s]=a:delete r.current[s]}},scrollToField:function(l){let a=arguments.length>1&&arguments[1]!==void 0?arguments[1]:{};const s=te(l),c=_e(s,n.__INTERNAL__.name),i=c?document.getElementById(c):null;i&&wt(i,Object.assign({scrollMode:"if-needed",block:"nearest"},a))},getFieldInstance:l=>{const a=Oe(l);return r.current[a]}}),[e,t]);return[n]}var Tt=globalThis&&globalThis.__rest||function(e,t){var r={};for(var n in e)Object.prototype.hasOwnProperty.call(e,n)&&t.indexOf(n)<0&&(r[n]=e[n]);if(e!=null&&typeof Object.getOwnPropertySymbols=="function")for(var l=0,n=Object.getOwnPropertySymbols(e);l<n.length;l++)t.indexOf(n[l])<0&&Object.prototype.propertyIsEnumerable.call(e,n[l])&&(r[n[l]]=e[n[l]]);return r};const Lt=(e,t)=>{const r=o.useContext(Be),n=o.useContext(Xe),{getPrefixCls:l,direction:a,form:s}=o.useContext(de),{prefixCls:c,className:i,rootClassName:f,size:g=r,disabled:j=n,form:T,colon:C,labelAlign:p,labelWrap:N,labelCol:x,wrapperCol:m,hideRequiredMark:E,layout:b="horizontal",scrollToFirstError:$,requiredMark:d,onFinishFailed:v,name:I}=e,L=Tt(e,["prefixCls","className","rootClassName","size","disabled","form","colon","labelAlign","labelWrap","labelCol","wrapperCol","hideRequiredMark","layout","scrollToFirstError","requiredMark","onFinishFailed","name"]),y=o.useMemo(()=>d!==void 0?d:s&&s.requiredMark!==void 0?s.requiredMark:!E,[E,d,s]),w=C??(s==null?void 0:s.colon),R=l("form",c),[q,u]=ge(R),B=Q(R,{[`${R}-${b}`]:!0,[`${R}-hide-required-mark`]:y===!1,[`${R}-rtl`]:a==="rtl",[`${R}-${g}`]:g},u,i,f),[z]=Te(T),{__INTERNAL__:P}=z;P.name=I;const O=o.useMemo(()=>({name:I,labelAlign:p,labelCol:x,labelWrap:N,wrapperCol:m,vertical:b==="vertical",colon:w,requiredMark:y,itemRef:P.itemRef,form:z}),[I,p,x,m,b,w,y,z]);o.useImperativeHandle(t,()=>z);const A=(V,H)=>{if(V){let h={block:"nearest"};typeof V=="object"&&(h=V),z.scrollToField(H,h)}},X=V=>{if(v==null||v(V),V.errorFields.length){const H=V.errorFields[0].name;if($!==void 0){A($,H);return}s&&s.scrollToFirstError!==void 0&&A(s.scrollToFirstError,H)}};return q(o.createElement(Ge,{disabled:j},o.createElement(Ke,{size:g},o.createElement(J.Provider,{value:O},o.createElement(Qe,Object.assign({id:I},L,{name:I,onFinishFailed:X,form:z,className:B}))))))},Wt=o.forwardRef(Lt),zt=Wt,Le=()=>{const{status:e,errors:t=[],warnings:r=[]}=o.useContext(fe);return{status:e,errors:t,warnings:r}};Le.Context=fe;const Vt=Le;function Ht(e){const[t,r]=o.useState(e),n=o.useRef(null),l=o.useRef([]),a=o.useRef(!1);o.useEffect(()=>(a.current=!1,()=>{a.current=!0,xe.cancel(n.current),n.current=null}),[]);function s(c){a.current||(n.current===null&&(l.current=[],n.current=xe(()=>{n.current=null,r(i=>{let f=i;return l.current.forEach(g=>{f=g(f)}),f})})),l.current.push(c))}return[t,s]}function qt(){const{itemRef:e}=o.useContext(J),t=o.useRef({});function r(n,l){const a=l&&typeof l=="object"&&l.ref,s=n.join("_");return(t.current.name!==s||t.current.originRef!==a)&&(t.current.name=s,t.current.originRef=a,t.current.ref=Ye(e(n),a)),t.current.ref}return r}const At=e=>{const{prefixCls:t,status:r,wrapperCol:n,children:l,errors:a,warnings:s,_internalItemRender:c,extra:i,help:f,fieldId:g,marginBottom:j,onErrorVisibleChanged:T}=e,C=`${t}-item`,p=o.useContext(J),N=n||p.wrapperCol||{},x=Q(`${C}-control`,N.className),m=o.useMemo(()=>Object.assign({},p),[p]);delete m.labelCol,delete m.wrapperCol;const E=o.createElement("div",{className:`${C}-control-input`},o.createElement("div",{className:`${C}-control-input-content`},l)),b=o.useMemo(()=>({prefixCls:t,status:r}),[t,r]),$=j!==null||a.length||s.length?o.createElement("div",{style:{display:"flex",flexWrap:"nowrap"}},o.createElement(ue.Provider,{value:b},o.createElement(Re,{fieldId:g,errors:a,warnings:s,help:f,helpStatus:r,className:`${C}-explain-connected`,onVisibleChanged:T})),!!j&&o.createElement("div",{style:{width:0,height:j}})):null,d={};g&&(d.id=`${g}_extra`);const v=i?o.createElement("div",Object.assign({},d,{className:`${C}-extra`}),i):null,I=c&&c.mark==="pro_table_render"&&c.render?c.render(e,{input:E,errorList:$,extra:v}):o.createElement(o.Fragment,null,E,$,v);return o.createElement(J.Provider,{value:m},o.createElement(Me,Object.assign({},N,{className:x}),I))},Dt=At;var Bt={icon:{tag:"svg",attrs:{viewBox:"64 64 896 896",focusable:"false"},children:[{tag:"path",attrs:{d:"M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z"}},{tag:"path",attrs:{d:"M623.6 316.7C593.6 290.4 554 276 512 276s-81.6 14.5-111.6 40.7C369.2 344 352 380.7 352 420v7.6c0 4.4 3.6 8 8 8h48c4.4 0 8-3.6 8-8V420c0-44.1 43.1-80 96-80s96 35.9 96 80c0 31.1-22 59.6-56.1 72.7-21.2 8.1-39.2 22.3-52.1 40.9-13.1 19-19.9 41.8-19.9 64.9V620c0 4.4 3.6 8 8 8h48c4.4 0 8-3.6 8-8v-22.7a48.3 48.3 0 0130.9-44.8c59-22.7 97.1-74.7 97.1-132.5.1-39.3-17.1-76-48.3-103.3zM472 732a40 40 0 1080 0 40 40 0 10-80 0z"}}]},name:"question-circle",theme:"outlined"};const Xt=Bt;var We=function(t,r){return o.createElement(Ue,$e($e({},t),{},{ref:r,icon:Xt}))};We.displayName="QuestionCircleOutlined";const Gt=o.forwardRef(We);var Kt=globalThis&&globalThis.__rest||function(e,t){var r={};for(var n in e)Object.prototype.hasOwnProperty.call(e,n)&&t.indexOf(n)<0&&(r[n]=e[n]);if(e!=null&&typeof Object.getOwnPropertySymbols=="function")for(var l=0,n=Object.getOwnPropertySymbols(e);l<n.length;l++)t.indexOf(n[l])<0&&Object.prototype.propertyIsEnumerable.call(e,n[l])&&(r[n[l]]=e[n[l]]);return r};function Qt(e){return e?typeof e=="object"&&!o.isValidElement(e)?e:{title:e}:null}const Yt=e=>{let{prefixCls:t,label:r,htmlFor:n,labelCol:l,labelAlign:a,colon:s,required:c,requiredMark:i,tooltip:f}=e;var g;const[j]=Je("Form"),{vertical:T,labelAlign:C,labelCol:p,labelWrap:N,colon:x}=o.useContext(J);if(!r)return null;const m=l||p||{},E=a||C,b=`${t}-item-label`,$=Q(b,E==="left"&&`${b}-left`,m.className,{[`${b}-wrap`]:!!N});let d=r;const v=s===!0||x!==!1&&s!==!1;v&&!T&&typeof r=="string"&&r.trim()!==""&&(d=r.replace(/[:|：]\s*$/,""));const L=Qt(f);if(L){const{icon:w=o.createElement(Gt,null)}=L,R=Kt(L,["icon"]),q=o.createElement(Ze,Object.assign({},R),o.cloneElement(w,{className:`${t}-item-tooltip`,title:""}));d=o.createElement(o.Fragment,null,d,q)}i==="optional"&&!c&&(d=o.createElement(o.Fragment,null,d,o.createElement("span",{className:`${t}-item-optional`,title:""},(j==null?void 0:j.optional)||((g=ke.Form)===null||g===void 0?void 0:g.optional))));const y=Q({[`${t}-item-required`]:c,[`${t}-item-required-mark-optional`]:i==="optional",[`${t}-item-no-colon`]:!v});return o.createElement(Me,Object.assign({},m,{className:$}),o.createElement("label",{htmlFor:n,className:y,title:typeof r=="string"?r:""},d))},Ut=Yt;var Jt=globalThis&&globalThis.__rest||function(e,t){var r={};for(var n in e)Object.prototype.hasOwnProperty.call(e,n)&&t.indexOf(n)<0&&(r[n]=e[n]);if(e!=null&&typeof Object.getOwnPropertySymbols=="function")for(var l=0,n=Object.getOwnPropertySymbols(e);l<n.length;l++)t.indexOf(n[l])<0&&Object.prototype.propertyIsEnumerable.call(e,n[l])&&(r[n[l]]=e[n[l]]);return r};const Zt={success:ht,warning:bt,error:rt,validating:yt};function kt(e){const{prefixCls:t,className:r,rootClassName:n,style:l,help:a,errors:s,warnings:c,validateStatus:i,meta:f,hasFeedback:g,hidden:j,children:T,fieldId:C,isRequired:p,onSubItemMetaChange:N}=e,x=Jt(e,["prefixCls","className","rootClassName","style","help","errors","warnings","validateStatus","meta","hasFeedback","hidden","children","fieldId","isRequired","onSubItemMetaChange"]),m=`${t}-item`,{requiredMark:E}=o.useContext(J),b=o.useRef(null),$=le(s),d=le(c),v=a!=null,I=!!(v||s.length||c.length),L=!!b.current&&et(b.current),[y,w]=o.useState(null);tt(()=>{if(I&&b.current){const P=getComputedStyle(b.current);w(parseInt(P.marginBottom,10))}},[I,L]);const R=P=>{P||w(null)},u=function(){let P=arguments.length>0&&arguments[0]!==void 0?arguments[0]:!1,O="";const A=P?$:f.errors,X=P?d:f.warnings;return i!==void 0?O=i:f.validating?O="validating":A.length?O="error":X.length?O="warning":(f.touched||g&&f.validated)&&(O="success"),O}(),B=o.useMemo(()=>{let P;if(g){const O=u&&Zt[u];P=O?o.createElement("span",{className:Q(`${m}-feedback-icon`,`${m}-feedback-icon-${u}`)},o.createElement(O,null)):null}return{status:u,errors:s,warnings:c,hasFeedback:g,feedbackIcon:P,isFormItemInput:!0}},[u,g]),z=Q(m,r,n,{[`${m}-with-help`]:v||$.length||d.length,[`${m}-has-feedback`]:u&&g,[`${m}-has-success`]:u==="success",[`${m}-has-warning`]:u==="warning",[`${m}-has-error`]:u==="error",[`${m}-is-validating`]:u==="validating",[`${m}-hidden`]:j});return o.createElement("div",{className:z,style:l,ref:b},o.createElement(pt,Object.assign({className:`${m}-row`},nt(x,["_internalItemRender","colon","dependencies","extra","fieldKey","getValueFromEvent","getValueProps","htmlFor","id","initialValue","isListField","label","labelAlign","labelCol","labelWrap","messageVariables","name","normalize","noStyle","preserve","required","requiredMark","rules","shouldUpdate","trigger","tooltip","validateFirst","validateTrigger","valuePropName","wrapperCol"])),o.createElement(Ut,Object.assign({htmlFor:C,required:p,requiredMark:E},e,{prefixCls:t})),o.createElement(Dt,Object.assign({},e,f,{errors:$,warnings:d,prefixCls:t,status:u,help:a,marginBottom:y,onErrorVisibleChanged:R}),o.createElement(je.Provider,{value:N},o.createElement(fe.Provider,{value:B},T)))),!!y&&o.createElement("div",{className:`${m}-margin-offset`,style:{marginBottom:-y}}))}const en="__SPLIT__",tn=o.memo(e=>{let{children:t}=e;return t},(e,t)=>e.value===t.value&&e.update===t.update&&e.childProps.length===t.childProps.length&&e.childProps.every((r,n)=>r===t.childProps[n]));function nn(e){return e!=null}function Fe(){return{errors:[],warnings:[],touched:!1,validating:!1,name:[],validated:!1}}function rn(e){const{name:t,noStyle:r,className:n,dependencies:l,prefixCls:a,shouldUpdate:s,rules:c,children:i,required:f,label:g,messageVariables:j,trigger:T="onChange",validateTrigger:C,hidden:p}=e,{getPrefixCls:N}=o.useContext(de),{name:x}=o.useContext(J),m=typeof i=="function",E=o.useContext(je),{validateTrigger:b}=o.useContext(ot),$=C!==void 0?C:b,d=nn(t),v=N("form",a),[I,L]=ge(v),y=o.useContext(lt),w=o.useRef(),[R,q]=Ht({}),[u,B]=it(()=>Fe()),z=h=>{const M=y==null?void 0:y.getKey(h.name);if(B(h.destroy?Fe():h,!0),r&&E){let F=h.name;if(h.destroy)F=w.current||F;else if(M!==void 0){const[_,W]=M;F=[_].concat(D(W)),w.current=F}E(h,F)}},P=(h,M)=>{q(F=>{const _=Object.assign({},F),G=[].concat(D(h.name.slice(0,-1)),D(M)).join(en);return h.destroy?delete _[G]:_[G]=h,_})},[O,A]=o.useMemo(()=>{const h=D(u.errors),M=D(u.warnings);return Object.values(R).forEach(F=>{h.push.apply(h,D(F.errors||[])),M.push.apply(M,D(F.warnings||[]))}),[h,M]},[R,u.errors,u.warnings]),X=qt();function V(h,M,F){return r&&!p?h:o.createElement(kt,Object.assign({key:"row"},e,{className:Q(n,L),prefixCls:v,fieldId:M,isRequired:F,errors:O,warnings:A,meta:u,onSubItemMetaChange:P}),h)}if(!d&&!m&&!l)return I(V(i));let H={};return typeof g=="string"?H.label=g:t&&(H.label=String(t)),j&&(H=Object.assign(Object.assign({},H),j)),I(o.createElement(at,Object.assign({},e,{messageVariables:H,trigger:T,validateTrigger:$,onMetaChange:z}),(h,M,F)=>{const _=te(t).length&&M?M.name:[],W=_e(_,x),G=f!==void 0?f:!!(c&&c.some(S=>{if(S&&typeof S=="object"&&S.required&&!S.warningOnly)return!0;if(typeof S=="function"){const Z=S(F);return Z&&Z.required&&!Z.warningOnly}return!1})),K=Object.assign({},h);let U=null;if(Array.isArray(i)&&d)U=i;else if(!(m&&(!(s||l)||d))){if(!(l&&!m&&!d))if(st(i)){const S=Object.assign(Object.assign({},i.props),K);if(S.id||(S.id=W),e.help||O.length>0||A.length>0||e.extra){const k=[];(e.help||O.length>0)&&k.push(`${W}_help`),e.extra&&k.push(`${W}_extra`),S["aria-describedby"]=k.join(" ")}O.length>0&&(S["aria-invalid"]="true"),G&&(S["aria-required"]="true"),ct(i)&&(S.ref=X(_,i)),new Set([].concat(D(te(T)),D(te($)))).forEach(k=>{S[k]=function(){for(var pe,he,ie,be,ae,ye=arguments.length,se=new Array(ye),re=0;re<ye;re++)se[re]=arguments[re];(ie=K[k])===null||ie===void 0||(pe=ie).call.apply(pe,[K].concat(se)),(ae=(be=i.props)[k])===null||ae===void 0||(he=ae).call.apply(he,[be].concat(se))}});const ne=[S["aria-required"],S["aria-invalid"],S["aria-describedby"]];U=o.createElement(tn,{value:K[e.valuePropName||"value"],update:i,childProps:ne},mt(i,S))}else m&&(s||l)&&!d?U=i(F):U=i}return V(U,W,G)}))}const ze=rn;ze.useStatus=Vt;const on=ze;var ln=globalThis&&globalThis.__rest||function(e,t){var r={};for(var n in e)Object.prototype.hasOwnProperty.call(e,n)&&t.indexOf(n)<0&&(r[n]=e[n]);if(e!=null&&typeof Object.getOwnPropertySymbols=="function")for(var l=0,n=Object.getOwnPropertySymbols(e);l<n.length;l++)t.indexOf(n[l])<0&&Object.prototype.propertyIsEnumerable.call(e,n[l])&&(r[n[l]]=e[n[l]]);return r};const an=e=>{var{prefixCls:t,children:r}=e,n=ln(e,["prefixCls","children"]);const{getPrefixCls:l}=o.useContext(de),a=l("form",t),s=o.useMemo(()=>({prefixCls:a,status:"error"}),[a]);return o.createElement(ut,Object.assign({},n),(c,i,f)=>o.createElement(ue.Provider,{value:s},r(c.map(g=>Object.assign(Object.assign({},g),{fieldKey:g.key})),i,{errors:f.errors,warnings:f.warnings})))},sn=an;function cn(){const{form:e}=o.useContext(J);return e}const Y=zt;Y.Item=on;Y.List=sn;Y.ErrorList=Re;Y.useForm=Te;Y.useFormInstance=cn;Y.useWatch=dt;Y.Provider=ft;Y.create=()=>{};const fn=Y;export{fn as F};
