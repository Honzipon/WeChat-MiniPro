<template>
  <div>
    <el-button type="primary" @click="handleAdd">新增场馆</el-button>
    <el-table :data="venues" border style="margin-top: 20px">
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="场馆名称"></el-table-column>
      <el-table-column prop="address" label="地址"></el-table-column>
      <el-table-column prop="totalSeats" label="座位总数"></el-table-column>
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="场馆名称">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address"></el-input>
        </el-form-item>
        <el-form-item label="座位总数">
          <el-input-number v-model="form.totalSeats"></el-input-number>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      venues: [],
      dialogVisible: false,
      dialogTitle: '',
      form: { id: null, name: '', address: '', totalSeats: 0 }
    }
  },
  created() {
    this.fetchVenues()
  },
  methods: {
    fetchVenues() {
  this.$http.get('/venue/list').then(res => {
    console.log('原始响应:', res);
    // 从响应中提取数据数组
    if (res && res.code === 0 && Array.isArray(res.data)) {
      this.venues = res.data;   // 赋值给表格绑定的变量
    } else if (Array.isArray(res)) {
      this.venues = res;
    } else {
      this.venues = [];
      console.warn('数据格式异常', res);
    }
    console.log('场馆列表（处理后）:', this.venues);
  }).catch(err => {
    console.error('获取场馆列表失败', err);
    this.venues = [];
  });
},
    handleAdd() {
      this.dialogTitle = '新增场馆'
      this.form = { id: null, name: '', address: '', totalSeats: 0 }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑场馆'
      this.form = { ...row }
      this.dialogVisible = true
    },
    submitForm() {
      if (this.form.id) {
        this.$http.put('/venue/update', this.form).then(() => {
          this.$message.success('更新成功')
          this.dialogVisible = false
          this.fetchVenues()
        })
      } else {
        this.$http.post('/venue/add', this.form).then(() => {
          this.$message.success('添加成功')
          this.dialogVisible = false
          this.fetchVenues()
        })
      }
    },
    handleDelete(id) {
      this.$confirm('确定删除该场馆吗？', '提示', { type: 'warning' }).then(() => {
        this.$http.delete(`/venue/delete/${id}`).then(() => {
          this.$message.success('删除成功')
          this.fetchVenues()
        })
      })
    }
  }
}
</script>