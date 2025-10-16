import { Route, Routes } from "react-router-dom";
import Welcome from "./pages/Welcome";
import Sign_up from "./pages/Sign_up"
import Sign_in from "./pages/Sign_in"
import Admin_home from "./pages/Admin_home"
import Admin_Orders from "./pages/Admin_Orders"
import Customer_home from "./pages/Customer_home"
import Customer_Orders from "./pages/Customer_Orders"
import User_manage from "./pages/User_manage"
import Product_manage from "./pages/Product_manage"
import Add_product from "./pages/product/Add_product";
import Update_product from "./pages/product/Update_product";
import Delete_product from "./pages/product/Delete_product";
import Search_product from "./pages/product/Search_product";
import Cart from "./pages/Cart";
import OrderSummary from "./pages/OrderSummary";
import ProtectedRoute from "./components/ProtectedRoute";


function App() {

  return (
    <div className="app">
      <Routes>
          <Route path = "/" element = {<Welcome />} />
          <Route path = "/sign_up" element = {<Sign_up />} />
          <Route path = "/sign_in" element = {<Sign_in />} />

          <Route path = "/admin_home" element = {
            <ProtectedRoute adminOnly>
              <Admin_home />
            </ProtectedRoute>
          } />
          <Route path="/admin-orders" element={
            <ProtectedRoute adminOnly>
              <Admin_Orders />
            </ProtectedRoute>
          } />

          <Route path = "/customer_home" element = {
            <ProtectedRoute>
              <Customer_home />
            </ProtectedRoute>
          } />
          <Route path = "/customer_Orders" element = {
            <ProtectedRoute>
              <Customer_Orders />
            </ProtectedRoute>
          } />

          <Route path = "/um" element = {
            <ProtectedRoute adminOnly>
              <User_manage />
            </ProtectedRoute>
          } />
          <Route path = "/pm" element = {
            <ProtectedRoute adminOnly>
              <Product_manage />
            </ProtectedRoute>
          } />

          <Route path = "/addProd" element = {
            <ProtectedRoute adminOnly>
              <Add_product />
            </ProtectedRoute>
          } />
          <Route path = "/updateProd" element = {
            <ProtectedRoute adminOnly>
              <Update_product />
            </ProtectedRoute>
          } />
          <Route path = "/deleteProd" element = {
            <ProtectedRoute adminOnly>
              <Delete_product />
            </ProtectedRoute>
          } />
          <Route path = "/searchProd" element = {
            <ProtectedRoute adminOnly>
              <Search_product />
            </ProtectedRoute>
          } />

          <Route path="/cart" element={
            <ProtectedRoute>
              <Cart />
            </ProtectedRoute>
          } />
          <Route path="/order-summary/:orderId" element={
            <ProtectedRoute>
              <OrderSummary />
            </ProtectedRoute>
          } />

      </Routes>
    </div>
  )
}

export default App
