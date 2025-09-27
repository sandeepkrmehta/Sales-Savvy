import {BrowserRouter, Routes, Route} from 'react-router-dom'
import Welcome from './pages/Welcome' 
import Signup from './pages/Signup' 
import Signin from './pages/Signin' 
import AdminHome from './pages/AdminHome';
import Customer from './pages/Customer';
import Addproduct from './pages/Addproduct';
import Updateproduct from './pages/Updateproduct';
import Cart from './pages/Cart';
import OrderSummary from './pages/OrderSummary';
import Navigation from './components/Navigation';
import './styles.css';

function App() {
  return (
    <BrowserRouter>
      <Navigation />
      <Routes>
          <Route path='/' element = {<Welcome />} />
          <Route path='/sign_up_page' element = {<Signup />} />
          <Route path='/sign_in_page' element = {<Signin />} />
          <Route path='/admin_page' element = {<AdminHome />} />
          <Route path='/customer_page' element = {<Customer />} />
          <Route path='/add_prod_page' element = {<Addproduct />} />
          <Route path='/update_prod_page' element = {<Updateproduct />} />
           <Route path="/cart" element={<Cart />} />
          <Route path="/order-summary/:orderId" element={<OrderSummary />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;