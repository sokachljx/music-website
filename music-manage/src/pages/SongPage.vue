<template>
  <div class="table">
    <div class="crumbs">
      <i class="el-icon-tickets">歌曲信息</i>
    </div>
    <div cxlass="container">
      <div class="handle-box">
        <el-button
          class="handle-del mr10"
          type="primary"
          size="mini"
          @click="delAll"
          >批量删除</el-button
        >
        <el-input
          v-model="select_word"
          class="handle-input mr10"
          size="mini"
          placeholder="请输入歌曲名"
        ></el-input>
        <el-button
          type="primary"
          size="mini"
          @click="centerDialogVisible = true"
          >添加歌曲</el-button
        >
      </div>
      <el-table
        size="mini"
        border
        style="width:100%"
        height="440px"
        :data="data"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="40"></el-table-column>
        <el-table-column label="歌曲图片" width="110" align="center">
          <template slot-scope="scope">
            <div class="song-img">
              <img :src="getUrl(scope.row.pic)" alt="" style="width:100%" />
            </div>
            <div class="play" @click="setSongUrl(scope.row.url, scope.row.name)">
              <div v-if="toggle==scope.row.name">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-zanting"></use>
                </svg>
              </div>
              <div v-if="toggle!=scope.row.name">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-bofanganniu"></use>
                </svg>
              </div>
            </div>
            
          </template>
        </el-table-column>
        <el-table-column
          prop="name"
          label="歌手-歌名"
          width="120"
          align="center"
        ></el-table-column>

        <el-table-column label="专辑">
          <template slot-scope="scope">
            <p style="height: 100px; overflow: scroll">
              {{ scope.row.introduction }}
            </p>
          </template>
        </el-table-column>
        <el-table-column label="歌词" align="center">
          <template slot-scope="scope">
            <ul style="height: 100px; overflow: scroll">
              <li>
              <li v-for="(item, index) in parseLyric(scope.row.lyric)" :key="index">
                {{ item}}
              </li>
            </ul>
          </template>
        </el-table-column>
        <el-table-column label="资源更新" width="100" align="center">
          <template slot-scope="scope">
            <el-upload
              class="upload-demo"
              :action="uploadUrl(scope.row.id)"
              :show-file-list="false"
              :on-success="handleAvatorSuccess"
              :before-upload="beforeAvatorUpload"
              >
                <el-button size="mini">更新图片</el-button>
            </el-upload>
            <br>
            <el-upload
              class="upload-demo change"
              :action="uploadSongUrl(scope.row.id)"
              :show-file-list="false"
              :on-success="handleSongSuccess"
              :before-upload="beforeSongUpload">
              <el-button size="mini">更新歌曲</el-button>
            </el-upload>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)"
              >编辑</el-button
            >
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.row.id)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
          background
          layout="total, prev, pager, next"
          :current-page="currentPage"
          :page-size="pageSize"
          :total="tableData.length"
          @current-change="handleCurrentChange"
        >
        </el-pagination>
      </div>
    </div>
    <el-dialog
      title="添加歌曲"
      :visible.sync="centerDialogVisible"
      width="400px"
      center
    >
      <el-form
        :model="registerForm"
        ref="registerForm"
        label-width="80px"
        action=""
        id="tf"
      >
        <div>
          <label>歌曲名</label>
          <el-input
            type="text"
            name="name"
            v-model="registerForm.name"
          ></el-input>
        </div>
        <div>
          <label>专辑</label>
          <el-input
            type="text"
            
            name="introduction"
            v-model="registerForm.introduction"
          ></el-input>
        </div>
        <div>
          <label>歌词</label>
          <el-input
            type="textarea"
            
            name="lyric"
            v-model="registerForm.lyric"
          ></el-input>
        </div>
        <div>
          <label>歌曲上传</label>
          <br />
          <input type="file" name="file" id="upadte-file-input" />
        </div>
      </el-form>
      <span class="dialog-footer" slot="footer">
        <el-button size="mini" @click="centerDialogVisible = false"
          >取 消</el-button
        >
        <el-button type="primary" size="mini" @click="addSong">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 编辑弹出框 -->
    <el-dialog title="修改歌曲" :visible.sync="editVisible" width="400px">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item prop="name" label="歌手-歌曲" size="mini">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item prop="introduction" label="专辑" size="mini">
          <el-input v-model="form.introduction"></el-input>
        </el-form-item>
        <el-form-item prop="lyric" label="歌词" size="mini">
          <el-input type="textarea" v-model="form.lyric" placeholder="歌词"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="editVisible = false">取 消</el-button>
        <el-button type="primary" size="mini" @click="saveEdit"
          >确 定</el-button
        >
      </span>
    </el-dialog>

    <!-- 删除提示框 -->
    <el-dialog title="提示" :visible.sync="delVisible" width="300px" center>
      <div class="del-dialog-cnt" align="center">
        删除不可恢复，是否确定删除？
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="delVisible = false">取 消</el-button>
        <el-button type="primary" size="mini" @click="deleteRow"
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>

import { mixin } from "../mixins";
import {mapGetters} from 'vuex';
import  '../assets/js/iconfont.js'
import { songOfSingerId, updateSong ,delSong} from "../api/index";
export default {
  mixins: [mixin],
  data() {
    return {
      singerId: "",
      singerName: "",
      centerDialogVisible: false,
      editVisible: false, //编辑弹窗是否显示
      delVisible: false, //删除弹窗是否显示
      multipleSelection: [], //删除多选
      registerForm: {
        //添加框
        name: "",
        singerName: "",
        introduction: "",
        lyric: ""
      },
      form: {
        // 编辑框信息
        id: "",
        singerId: "",
        name: "",
        introduction: "",
        createTime: "",
        updateTime: "",
        pic: "",
        lyric: "",
        url: ""
      },
      tableData: [],
      tempDate: [],
      select_word: "",
      pageSize: 5, // 页数
      currentPage: 1, // 当前页
      idx: -1, //当前选择项
      toggle: false,   //播放器的显示图标状态
    };
  },
  computed: {

  ...mapGetters([
            'isPlay'
        ]),
    // 计算当前表格中的数据
    data() {
      return this.tableData.slice(
        (this.currentPage - 1) * this.pageSize,
        this.currentPage * this.pageSize
      );
    }
  },
  //搜索框的内容发生变化的时候，搜索结果table列表的内容跟着他的内容发生变化
  watch: {
    select_word: function() {
      if (this.select_word === "") {
        this.tableData = this.tempDate;
      } else {
        this.tableData = [];
        for (let item of this.tempDate) {
          if (item.name.includes(this.select_word)) {
            this.tableData.push(item);
          }
        }
      }
    }
  },
  created() {
    this.singerId = this.$route.query.id;
    this.singerName = this.$route.query.name;
    this.getData();
  },
  destoryed(){
    this.$store.commit('setIsPlay',false);
  },
  methods: {
    // 查询所有歌曲
    getData() {
      this.tableData = [];
      this.tempDate = [];
      var _this=this;
      songOfSingerId(this.singerId).then((res) => {
        this.tableData = res;
        this.tempDate = res;
        this.currentPage = 1;
        this.$store.commit('setUrl',_this.$store.state.HOST+res[0].url);
        // console.log(_this.$store.state.HOST+res[0].url)
       
      }).catch(err => {
        console.log(err)
      })
    },
    // 添加歌曲
    addSong() {
      let _this = this;
      var form = new FormData(document.getElementById("tf"));
      form.append("singerId", this.singerId);
      form.set("name", this.singerName + "-" + form.get("name"));
      if (!form.get("lyric")) {
        form.set("lyric", "[00:00:00]暂无歌词");
      }
      var req = new XMLHttpRequest();
      req.onreadystatechange = function() {
        // req.readyState == 4 获取到返回的完整数据
        if (req.readyState == 4 && req.status == 200) {
          let res = JSON.parse(req.response);
          if (res.code) {
            _this.getData();
            _this.registerForm = {};
            _this.notify(res.msg, "success");
          }else {
            _this.notify("添加失败", "error");
          }
        }
      }
      req.open("post", `${_this.$store.state.HOST}/song/add`, false);
      req.send(form);
      _this.centerDialogVisible = false;
    },
    // 更新图片
    uploadUrl(id) {
         return `${this.$store.state.HOST}/song/updateSongPic?id=${id}`;
    },
    // 获取当前页
    handleCurrentChange(val) {
      this.currentPage = val;
    },

    // 编辑
    handleEdit(row) {
      this.editVisible=true;
      this.form={
        id:row.id,
        name:row.name,
        introduction:row.introduction,
        lyric:row.lyric
      }
      this.editVisible=true;
    },
    // 保存编辑
    saveEdit() {
      let params=new URLSearchParams();
      params.append("id",this.form.id);
      params.append("name",this.form.name);
      params.append("introduction",this.form.introduction);
      params.append("lyric",this.form.lyric);
      updateSong(params)
      .then(res => {
        if(res.code == 1){
          this.getData();
          this.notify("修改成功","success");
        }else{
          this.notify("修改失败","error");
        }
      })
      // .catch(err => {
      //   console.log(err);
      // })
      this.editVisible = false;
    },
    // 确定删除
    deleteRow() {
      delSong(this.idx)
      .then(res=>{
        if(res){
          this.getData();
          this.notify("删除成功","success");
        }else{
          this.notify("删除失败","error");
        }
      })
      // .catch(err=>{
      //   console.log(err);
      // });
      this.delVisible = false;
    },
    // 解析歌词
    parseLyric (text) {
      let lines = text.split('\n')
      let pattern = /\[\d{2}:\d{2}.(\d{3}|\d{2})\]/g
      let result = []

      // 对于歌词格式不对的特殊处理
      if (!(/\[.+\]/.test(text))) {
        return [text]
      }
      for (let item of lines) {
        if (pattern.test(item)) {
          let value = item.replace(pattern, '') // 存歌词
          result.push(value)
        }
      }
      return result
    },
    beforeSongUpload (file) {
      var testMsg = file.name.substring(file.name.lastIndexOf('.') + 1)
      if (testMsg != 'mp3') {
        this.$message({
          message: '上传文件只能是mp3格式！',
          type: 'error'
        });
        return false;
        
      }
      return true;
    },
   // 更新歌曲url
   uploadSongUrl(id){
     return `${this.$store.state.HOST}/song/updateSongUrl?id=${id}`
   },
   handleSongSuccess (res, file) {
      if (res.code == 1) {
        this.getData()
        this.notify('上传成功', 'success')
      } else {
        this.notify('上传失败', 'error')
      }
    },
    //切换播放歌曲
    setSongUrl(url,name){
      this.toggle=name;
      this.$store.commit('setUrl',"http://127.0.0.1:3333"+url);
      if(this.isPlay){
        this.$store.commit('setIsPlay',false);
      }else{
        this.$store.commit('setIsPlay',true); 
      }
    }

  }
   
};
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}
.song-img {
  width: 100%;
  height: 80px;
  border-radius: 5px;
  margin-bottom: 5px;
  overflow: hidden;
}
.handle-input {
  width: 300px;
  display: inline-block;
}
.pagination {
  display: flex;
  justify-content: center;
}
.play {
    position: absolute;
    z-index: 100;
    width: 80px;
    height: 80px;
    top: 18px;
    left: 15px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
}
.icon {
  width: 2em;
  height: 2em;
  color: white;
  fill: currentColor;
  overflow: hidden;
}
</style>
