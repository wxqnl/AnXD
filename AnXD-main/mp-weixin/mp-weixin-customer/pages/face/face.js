// "use strict";
// const common_vendor = require("../../common/vendor.js");
// const hooks_useTheme = require("../../hooks/useTheme.js");

// if (!Array) {
//   const _easycom_tm_text2 = common_vendor.resolveComponent("tm-text");
//   const _easycom_tm_input2 = common_vendor.resolveComponent("tm-input");
//   const _easycom_tm_button2 = common_vendor.resolveComponent("tm-button");
//   const _easycom_tm_card2 = common_vendor.resolveComponent("tm-card");
//   const _easycom_tabbar_nav2 = common_vendor.resolveComponent("tabbar-nav");
//   const _easycom_tm_app2 = common_vendor.resolveComponent("tm-app");
//   (_easycom_tm_text2 + _easycom_tm_input2 + _easycom_tm_button2 + _easycom_tm_card2 + _easycom_tabbar_nav2 + _easycom_tm_app2)();
// }

// const _easycom_tm_text = () => "../../tmui/components/tm-text/tm-text.js";
// const _easycom_tm_input = () => "../../tmui/components/tm-input/tm-input.js";
// const _easycom_tm_button = () => "../../tmui/components/tm-button/tm-button.js";
// const _easycom_tm_card = () => "../../tmui/components/tm-card/tm-card.js";
// const _easycom_tabbar_nav = () => "../../components/tabbar-nav/tabbar-nav.js";
// const _easycom_tm_app = () => "../../tmui/components/tm-app/tm-app.js";

// if (!Math) {
//   (_easycom_tm_text + _easycom_tm_input + _easycom_tm_button + _easycom_tm_card + _easycom_tabbar_nav + _easycom_tm_app)();
// }

// const _sfc_main = common_vendor.defineComponent({
//   __name: "emotion-detection",
//   setup(__props) {
//     const { themeColor } = hooks_useTheme.useTheme();

//     // 页面数据
//     const headerText = common_vendor.ref("情绪检测");
//     const promptText = common_vendor.ref("请输入你的感受...");
//     const userInput = common_vendor.ref("");
//     const showResult = common_vendor.ref(false);
//     const resultText = common_vendor.ref("");
//     const resultData = common_vendor.ref({});

//     // 更新输入内容
//     function updateInput(event) {
//       userInput.value = event.detail.value;
//     }

//     // 提交情绪检测
//     function submitEmotion() {
//       if (!userInput.value.trim()) {
//         common_vendor.index.showToast({
//           title: "请输入你的感受",
//           icon: "none"
//         });
//         return;
//       }

//       // 模拟情绪检测（实际项目需替换为真实 API）
//       const emotion = detectEmotion(userInput.value);
//       resultText.value = `检测到的情绪: ${emotion}`;
//       resultData.value = {
//         emotion: emotion,
//         confidence: 0.8
//       };
//       showResult.value = true;
//     }

//     // 模拟情绪检测逻辑
//     function detectEmotion(text) {
//       if (text.includes("开心") || text.includes("快乐")) {
//         return "开心";
//       } else if (text.includes("难过") || text.includes("伤心")) {
//         return "悲伤";
//       } else if (text.includes("生气") || text.includes("愤怒")) {
//         return "愤怒";
//       } else {
//         return "中性";
//       }
//     }

//     return (_ctx, _cache) => {
//       return {
//         headerText: common_vendor.unref(headerText),
//         promptText: common_vendor.unref(promptText),
//         userInput: common_vendor.unref(userInput),
//         showResult: common_vendor.unref(showResult),
//         resultText: common_vendor.unref(resultText),
//         resultData: common_vendor.unref(resultData),
//         updateInput: common_vendor.o(updateInput),
//         submitEmotion: common_vendor.o(submitEmotion)
//       };
//     };
//   }
// });

// const MiniProgramPage = common_vendor._export_sfc(_sfc_main, [["__file", "src/pages/face/face.vue"]]);
// wx.createPage(MiniProgramPage);




// Page({
//   data: {
//     emotion: '开心', // 初始情绪
//     time: '', // 当前时间
//     showFeedback: false,
//     feedbackContent: ''
//   },

//   onLoad() {
//     const now = new Date();
//     const formattedTime = `${now.getFullYear()}-${now.getMonth()+1}-${now.getDate()} ${now.getHours()}:${now.getMinutes()}:${now.getSeconds()}`;
//     this.setData({
//       time: formattedTime
//     });

//     // 你也可以在这里发起接口请求更新emotion
//   },

//   openFeedback() {
//     this.setData({
//       showFeedback: true
//     });
//   },

//   cancelFeedback() {
//     this.setData({
//       showFeedback: false,
//       feedbackContent: ''
//     });
//   },

//   submitFeedback() {
//     const content = this.data.feedbackContent.trim();
//     if (!content) {
//       wx.showToast({
//         title: '请输入内容',
//         icon: 'none'
//       });
//       return;
//     }

//     // 这里可以发送反馈内容到服务器
//     console.log('用户反馈：', content);

//     wx.showToast({
//       title: '反馈已提交',
//       icon: 'success'
//     });

//     this.setData({
//       showFeedback: false,
//       feedbackContent: ''
//     });
//   }
// });




let timer = null; // 声明一个全局定时器变量
// 如果需要使用 takeCarInfoStore，请取消下面的注释并确保正确导入
// const takeCarInfo = store_modules_takeCarInfo.useTakeCarInfoStore();

Page({
  data: {
    orderId: '2',  // 示例订单号，可以从页面跳转参数中获取
    emotion: '',
    imageUrl: '',
    time: '',
    showFeedback: false,
    feedbackContent: ''
  },

  onLoad(options) {
    // 设置当前时间
    const now = new Date();
    const formattedTime = `${now.getFullYear()}-${now.getMonth() + 1}-${now.getDate()} ${now.getHours()}:${now.getMinutes()}:${now.getSeconds()}`;
    this.setData({
      time: formattedTime
    });

    // 处理订单ID
    if (options.orderId) {
      // 使用传入的订单ID
      this.setData({
        orderId: options.orderId
      }, () => {
        this.startPolling();
      });
    } else {
      // 尝试从缓存获取订单ID
      const storedOrderId = wx.getStorageSync('orderId');
      if (storedOrderId) {
        this.setData({
          orderId: storedOrderId
        });
      }
      // 无论是否有订单ID都开始轮询，因为 data 中已有默认值
      this.startPolling();
    }
  },

  startPolling() {
    // 先立即调用一次
    this.fetchAnalysisResult();

    // 然后设置定时轮询，比如每 5 秒请求一次
    timer = setInterval(() => {
      this.fetchAnalysisResult();
    }, 5000);
  },

  fetchAnalysisResult() {
    // 优先使用页面中的订单ID，如果没有则尝试从缓存获取
    const orderId = this.data.orderId || wx.getStorageSync('orderId');
    
    if (!orderId) {
      console.error('未找到订单ID');
      wx.showToast({
        title: '订单信息获取失败',
        icon: 'none'
      });
      return;
    }

    console.log('使用订单ID请求数据:', orderId);
    const url = `http://localhost:8600/customer-api/customer/getOrderAnalysisResult/${orderId}`;

    wx.request({
      url: url,
      method: 'GET',
      success: (res) => {
        if (res.data && res.data.code === 200) {
          const result = res.data.data;
          this.setData({
            emotion: result.emotionResult,
            imageUrl: result.imageUrl
          });
        } else {
          wx.showToast({
            title: '获取分析失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        console.error('接口请求失败', err);
        wx.showToast({
          title: '请求失败',
          icon: 'none'
        });
      }
    });
  },

  onUnload() {
    // 页面卸载时清除定时器
    if (timer) {
      clearInterval(timer);
      timer = null;
    }
  },
  
  onHide() {
    // 页面隐藏时也清除定时器（可选）
    if (timer) {
      clearInterval(timer);
      timer = null;
    }
  },
  
  openFeedback() {
    this.setData({
      showFeedback: true
    });
  },

  cancelFeedback() {
    this.setData({
      showFeedback: false,
      feedbackContent: ''
    });
  },

  submitFeedback() {
    // 获取反馈内容
    const { feedbackContent } = this.data;
    // 获取订单ID
    const orderId = this.data.orderId || wx.getStorageSync('orderId');
    
    if (!feedbackContent.trim()) {
      wx.showToast({
        title: '请输入内容',
        icon: 'none'
      });
      return;
    }
    
    if (!orderId) {
      wx.showToast({
        title: '订单信息获取失败',
        icon: 'none'
      });
      return;
    }
    
    wx.request({
      url: `http://localhost:8600/customer-api/customer/submitFeedback/${orderId}`,
      method: 'POST',
      data: feedbackContent, // 直接发送字符串作为请求体
      header: {
        'Content-Type': 'text/plain' // 修改为文本类型
      },
      success: (res) => {
        if (res.data && res.data.code === 200) {
          wx.showToast({
            title: '反馈已提交',
            icon: 'success'
          });
          this.setData({
            showFeedback: false,
            feedbackContent: ''
          });
        } else {
          wx.showToast({
            title: res.data.message || '提交失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        console.error('反馈提交失败', err);
        wx.showToast({
          title: '请求失败',
          icon: 'none'
        });
      }
    });
  }
});