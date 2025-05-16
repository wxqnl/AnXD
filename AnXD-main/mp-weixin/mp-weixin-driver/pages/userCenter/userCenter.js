"use strict";
const common_vendor = require("../../common/vendor.js");
require("../../tmui/tool/lib/tmpinia.js");
require("../../store/modules/tabBarNav.js");
const store_modules_user = require("../../store/modules/user.js");
require("../../tmui/tool/theme/theme.js");
require("../../tmui/tool/theme/colortool.js");
require("../../tmui/tool/lib/interface.js");
require("../../tmui/tool/function/util.js");
require("../../tmui/tool/function/preview.js");
require("../../utils/storage.js");
require("../../config/constant.js");
require("../../api/user/index.js");
require("../../http/index.js");
require("../../http/type.js");
if (!Array) {
  const _easycom_tm_image2 = common_vendor.resolveComponent("tm-image");
  const _easycom_tm_text2 = common_vendor.resolveComponent("tm-text");
  const _easycom_uni_icons2 = common_vendor.resolveComponent("uni-icons");
  const _easycom_tm_cell2 = common_vendor.resolveComponent("tm-cell");
  const _easycom_loading_button2 = common_vendor.resolveComponent("loading-button");
  const _easycom_tabbar_nav2 = common_vendor.resolveComponent("tabbar-nav");
  const _easycom_tm_app2 = common_vendor.resolveComponent("tm-app");
  (_easycom_tm_image2 + _easycom_tm_text2 + _easycom_uni_icons2 + _easycom_tm_cell2 + _easycom_loading_button2 + _easycom_tabbar_nav2 + _easycom_tm_app2)();
}
const _easycom_tm_image = () => "../../tmui/components/tm-image/tm-image.js";
const _easycom_tm_text = () => "../../tmui/components/tm-text/tm-text.js";
const _easycom_uni_icons = () => "../../uni_modules/uni-icons/components/uni-icons/uni-icons.js";
const _easycom_tm_cell = () => "../../tmui/components/tm-cell/tm-cell.js";
const _easycom_loading_button = () => "../../components/loading-button/loading-button.js";
const _easycom_tabbar_nav = () => "../../components/tabbar-nav/tabbar-nav.js";
const _easycom_tm_app = () => "../../tmui/components/tm-app/tm-app.js";
if (!Math) {
  (_easycom_tm_image + _easycom_tm_text + _easycom_uni_icons + _easycom_tm_cell + _easycom_loading_button + _easycom_tabbar_nav + _easycom_tm_app)();
}
const _sfc_main = /* @__PURE__ */ common_vendor.defineComponent({
  __name: "userCenter",
  setup(__props) {
    const useUser = store_modules_user.useUserStore();
    
    // 存储抓拍图片路径
    const photoPath = common_vendor.ref('');
    // 定时器 ID
    let captureInterval;

    const navList = common_vendor.ref([
      {
        name: "我的订单",
        icon: "iconfonthangchengdanxiao",
        path: "/pages/orderList/orderList",
        isNav: false
      },
      {
        name: "优惠券",
        icon: "iconfontyouhuiquan",
        path: "/pages/coupon/coupon",
        isNav: true
      }
    ]);
    function goPage(item) {
      console.log("item", item);
      if (item.isNav) {
        common_vendor.index.switchTab({
          url: item.path
        });
      } else {
        common_vendor.index.navigateTo({
          url: item.path
        });
      }
    }
    
    // 启动定时抓拍
    function startAutoCapture() {
      captureInterval = setInterval(() => {
        takePhoto(); // 每10秒抓拍一次
      }, 30000);
    }

    // 停止定时抓拍
    function stopAutoCapture() {
      clearInterval(captureInterval);
    }

    function takePhoto() {
      const cameraContext = common_vendor.index.createCameraContext(); // 获取相机上下文
      cameraContext.takePhoto({
        quality: 'high', // 高质量图片
        success: (res) => {
          // 拍照成功，获取临时文件路径
          photoPath.value = res.tempImagePath; // 存储照片路径
          console.log('拍照成功', res.tempImagePath);
          // 上传图片到后端接口
          //uploadImageToServer(res.tempImagePath);
        
        },
        fail: (err) => {
          // 拍照失败
          console.error('拍照失败', err);
          common_vendor.index.showToast({
            title: '拍照失败',
            icon: 'error',
          });
        }
      });
    }
     // 上传图片到后端接口
     function uploadImageToServer(imagePath) {
      common_vendor.index.uploadFile({
        url: 'http://localhost:8600/driver-api/cos/uploadImage', // 后端接口
        filePath: imagePath, // 临时文件路径
        name: 'file', // 后端接口参数名
        formData: {
          orderId: '1', // 订单ID
        },
        success: (res) => {
          if (res.statusCode === 200) {
            const data = JSON.parse(res.data);
            console.log('上传成功，图片 URL:', data.data);
            common_vendor.index.showToast({
              title: '图片上传成功',
              icon: 'success',
            });
          } else {
            console.error('上传失败', res);
            common_vendor.index.showToast({
              title: '上传失败',
              icon: 'error',
            });
          }
        },
        fail: (err) => {
          console.error('上传失败', err);
          common_vendor.index.showToast({
            title: '上传失败',
            icon: 'error',
          });
        }
      });
    }

    // 摄像头错误处理
    function onCameraError(e) {
      console.error('摄像头错误:', e);
      common_vendor.index.showToast({
        title: '摄像头错误',
        icon: 'error',
      });
    }

    // 页面加载时启动自动抓拍
    common_vendor.onShow(() => {
      startAutoCapture(); // 启动定时抓拍
    });

    // 页面卸载时停止抓拍
    common_vendor.onUnload(() => {
      stopAutoCapture(); // 清除定时器
    });
    function goLogin() {
      common_vendor.index.navigateTo({
        url: "/pages/login/login"
      });
    }
    common_vendor.onShow(() => {
      common_vendor.index.hideTabBar();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.p({
          preview: true,
          width: 250,
          height: 250,
          round: 25,
          src: common_vendor.unref(useUser).user.avatarUrl || "https://p26-passport.byteacctimg.com/img/user-avatar/39dc370feeaaddfc5dfda471b23de255~50x50.awebp"
        }),
        
        b: common_vendor.p({
          ["font-size"]: 50,
          label: common_vendor.unref(useUser).user.nickname || "未登录"
        }),
        c: common_vendor.f(navList.value, (item, k0, i0) => {
          return {
            a: "a21a4d8b-4-" + i0 + "," + ("a21a4d8b-3-" + i0),
            b: common_vendor.p({
              ["custom-prefix"]: "iconfont",
              type: item.icon,
              size: 30
            }),
            c: common_vendor.t(item.name),
            d: common_vendor.o(($event) => goPage(item), item.name),
            e: item.name,
            f: "a21a4d8b-3-" + i0 + ",a21a4d8b-0"
          };
        }),
        d: common_vendor.p({
          margin: [0, 0],
          titleFontSize: 30
        }),
        e: common_vendor.unref(useUser).token
      }, common_vendor.unref(useUser).token ? {
        f: common_vendor.p({
          block: true,
          ["click-fun"]: common_vendor.unref(useUser).logout,
          margin: [50],
          shadow: 0,
          size: "large",
          label: "退出登录"
        })
      } : {
        g: common_vendor.p({
          block: true,
          ["click-fun"]: goLogin,
          margin: [50],
          shadow: 0,
          size: "large",
          label: "登录"
        })
      });
    };
  }
});
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-a21a4d8b"], ["__file", "D:/work/daijia_work/web/mp-weixin-customer/src/pages/userCenter/userCenter.vue"]]);
wx.createPage(MiniProgramPage);