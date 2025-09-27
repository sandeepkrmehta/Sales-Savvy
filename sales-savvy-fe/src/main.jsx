import { createRoot } from 'react-dom/client'
import App from './App.jsx'
import {BrowserRouter} from 'react-router-dom'
import "./styles/root.css";
import "./styles/layout.css";
import "./styles/components.css";
import "./styles/user.css"
import "./styles/Cart.css"
import "./styles/CartDrawer.css"
import "./styles/Admin_home.css"
import "./styles/Customer_home.css"


createRoot(document.getElementById('root')).render(
    <BrowserRouter>
        <App />
    </BrowserRouter>
)
  