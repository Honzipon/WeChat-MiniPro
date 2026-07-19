<template>
  <div>
    <!-- 筛选栏 -->
    <el-form :inline="true" class="filter-form">
      <el-form-item label="订单状态">
        <el-select v-model="query.status" placeholder="全部" clearable @change="handleSearch">
          <el-option label="待支付" :value="0"></el-option>
          <el-option label="已支付" :value="1"></el-option>
          <el-option label="已完成" :value="2"></el-option>
          <el-option label="已取消" :value="3"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 订单列表表格 -->
    <el-table :data="orders" border stripe>
      <el-table-column prop="orderNo" label="订单号" width="200"></el-table-column>
      <el-table-column prop="userName" label="用户" width="120"></el-table-column>
      <el-table-column prop="matchName" label="赛事" width="200"></el-table-column>
      <el-table-column prop="seatInfo" label="座位信息" min-width="150"></el-table-column>
      <el-table-column prop="totalAmount" label="总金额" width="100">
        <template slot-scope="scope">
          ¥{{ scope.row.totalAmount }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="statusType(scope.row.status)">
            {{ statusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="下单时间" width="160"></el-table-column>
      <el-table-column label="操作" width="100">
        <template slot-scope="scope">
          <el-button type="text" @click="viewDetail(scope.row.orderNo)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="page"
      :page-sizes="[10, 20, 50]"
      :page-size="size"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total">
    </el-pagination>

    <!-- 订单详情弹窗 -->
    <el-dialog title="订单详情" :visible.sync="detailVisible" width="600px">
      <div v-if="currentOrder">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="订单号">{{ currentOrder.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="赛事">{{ currentOrder.matchName }}</el-descriptions-item>
          <el-descriptions-item label="座位">{{ currentOrder.seatInfo }}</el-descriptions-item>
          <el-descriptions-item label="总金额">¥{{ currentOrder.totalAmount }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ statusText(currentOrder.status) }}</el-descriptions-item>
          <el-descriptions-item label="下单时间">{{ currentOrder.createTime }}</el-descriptions-item>
          <el-descriptions-item label="支付时间">{{ currentOrder.payTime || '未支付' }}</el-descriptions-item>
          <el-descriptions-item label="联系人">{{ currentOrder.contactName }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ currentOrder.contactPhone }}</el-descriptions-item>
          <el-descriptions-item label="备注">{{ currentOrder.note || '无' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <span slot="footer">
        <el-button @click="detailVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      orders: [],
      total: 0,
      page: 1,
      size: 10,
      query: { status: '' },
      detailVisible: false,
      currentOrder: null
    }
  },
  created() {
    this.fetchOrders()
  },
  methods: {
    fetchOrders() {
      const params = {
        page: this.page,
        size: this.size,
        status: this.query.status
      }
      this.$http.get('/order/list', { params }).then(res => {
        // 根据实际响应结构调整：假设后端返回 {code, message, data: {list, total}}
        if (res && res.data) {
          this.orders = res.data.list || []
          this.total = res.data.total || 0
        } else if (Array.isArray(res)) {
          this.orders = res
          this.total = res.length
        } else {
          this.orders = []
          this.total = 0
        }
      }).catch(err => {
        console.error('获取订单列表失败', err)
      })
    },
    handleSearch() {
      this.page = 1
      this.fetchOrders()
    },
    resetSearch() {
      this.query.status = ''
      this.page = 1
      this.fetchOrders()
    },
    handleSizeChange(val) {
      this.size = val
      this.fetchOrders()
    },
    handleCurrentChange(val) {
      this.page = val
      this.fetchOrders()
    },
    viewDetail(orderNo) {
      this.$http.get(`/order/detail/${orderNo}`).then(res => {
        // 提取订单详情数据
        if (res && res.data) {
          this.currentOrder = res.data
        } else {
          this.currentOrder = res
        }
        this.detailVisible = true
      }).catch(err => {
        console.error('获取订单详情失败', err)
      })
    },
    statusText(status) {
      const map = { 0: '待支付', 1: '已支付', 2: '已完成', 3: '已取消' }
      return map[status] || '未知'
    },
    statusType(status) {
      const map = { 0: 'warning', 1: 'success', 2: 'info', 3: 'danger' }
      return map[status] || 'info'
    }
  }
}
</script>

<style scoped>
.filter-form {
  margin-bottom: 20px;
}
</style>