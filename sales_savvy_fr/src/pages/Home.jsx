import { Link } from 'react-router-dom'
import 'bootstrap/dist/css/bootstrap.min.css'

const Home = () => {
  return (
    <div className="home">
      {/* Hero Section */}
      <section 
        className="hero d-flex flex-column justify-content-center align-items-center text-center bg-light"
        style={{
          minHeight: '90vh',
          backgroundImage: 'url("https://images.unsplash.com/photo-1542291026-7eec264c27ff")',
          backgroundSize: 'cover',
          backgroundPosition: 'center',
          color: '#fff',
          position: 'relative',
        }}
      >
        {/* Overlay for better text contrast */}
        <div 
          className="overlay position-absolute top-0 start-0 w-100 h-100"
          style={{
            backgroundColor: 'rgba(0, 0, 0, 0.5)',
            zIndex: 0,
          }}
        ></div>

        {/* Content */}
        <div className="container position-relative" style={{ zIndex: 1 }}>
          <h1 className="display-3 fw-bold mb-3">Welcome to SalesSavvy</h1>
          <p className="lead mb-4">Your one-stop shop for all your needs</p>
          <Link to="/products" className="btn btn-primary btn-lg px-5 py-2">
            🛒 Shop Now
          </Link>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-5 bg-white">
        <div className="container text-center">
          <div className="row g-4">
            <div className="col-md-4">
              <div className="p-4 shadow-sm rounded">
                <i className="bi bi-truck display-4 text-primary mb-3"></i>
                <h4>Fast Delivery</h4>
                <p>Get your products delivered quickly and safely to your doorstep.</p>
              </div>
            </div>

            <div className="col-md-4">
              <div className="p-4 shadow-sm rounded">
                <i className="bi bi-shield-check display-4 text-success mb-3"></i>
                <h4>Secure Payments</h4>
                <p>We use Razorpay for 100% secure and trusted transactions.</p>
              </div>
            </div>

            <div className="col-md-4">
              <div className="p-4 shadow-sm rounded">
                <i className="bi bi-tags display-4 text-warning mb-3"></i>
                <h4>Best Prices</h4>
                <p>Find amazing deals and discounts on your favorite products.</p>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  )
}

export default Home;