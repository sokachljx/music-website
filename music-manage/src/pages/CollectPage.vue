<template>
  <div class="table">
    <div class="crumbs">
      <i class="el-icon-tickets">收藏信息</i>
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
          placeholder="筛选关键词"
        ></el-input>
        
      </div>
      <el-table
        size="mini"
        border
        style="width:100%"
        height="440px"
        :data="tableData"
        @selection-change="handleSelectionChange"
        ref="multipleTable"
      >
        <el-table-column type="selection" width="40"></el-table-column>
        
        <el-table-column
          prop="name"
          label="歌手-歌名"
          
          align="center"
        ></el-table-column>

        
        <el-table-column label="操作" width="150" align="center">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
    
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
import { songOfSongId,getCollectOfUserId,deleteCollection} from "../api/index";
export default {
  mixins: [mixin],
  props:['id'],
  data() {
    return {
      delVisible: false, //删除弹窗是否显示
      multipleSelection: [], //删除多选
      tableData: [],
      tempDate: [],
      select_word: "",
      idx: -1  //当前选择项
     
    };
  },
  
  //搜索框的内容发生变化的时候，搜索结果table列表的内容跟着他的内容发生变化
  watch: {
    select_word: function() {
      if (this.select_word == "") {
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
    this.getData();
  },
  
  methods: {
    // 查询该用户所有收藏歌曲
    getData() {
      this.tableData = [];
      this.tempDate = [];
      getCollectOfUserId(this.$route.query.id).then((res) => {
        for(let item of res){
          this.getSong(item.songId);
        }
      })
    },
    //根据歌曲id查询歌曲对象，放到tempData和tableData里面
    getSong(id){
      songOfSongId(id)
      .then(res=>{
        this.tempData.push(res);
        this.tableData.push(res);
        // console.log(this.tempDate)
      })
      .catch(err =>{
        console.log(err);
      });
    },
    
    // 删除一条歌曲
    deleteRow() {
     deleteCollection(this.$route.query.id,this.idx.id)
      .then(res=>{
        if(res){
          this.getData();
          this.notify("删除成功","success");
        }else{
          this.notify("删除失败","error");
        }
      })
      .catch(err=>{
        console.log(err);
      });
      this.delVisible = false;
    },
    //批量删除
        delAll() {
            for (let item of this.multipleSelection) {
                this.handleDelete(item);
                this.deleteRow();
            }
            this.multipleSelection = [];
        }
  }

}
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}

.handle-input {
  width: 300px;
  display: inline-block;
}

</style>
