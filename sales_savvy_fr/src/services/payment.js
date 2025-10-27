import API from './api'

export const paymentService = {
  createOrder: async (amount) => {
    const response = await API.post('/payment/create-order', {
      amount: amount,
      username: 'sandeep' // This will be replaced with actual username
    })
    return response.data
  },

  verifyPayment: async (paymentData) => {
    console.log('=== paymentData received in service ===');
    console.log('razorpay_order_id:', paymentData.razorpay_order_id);
    console.log('razorpay_payment_id:', paymentData.razorpay_payment_id);
    console.log('razorpay_signature:', paymentData.razorpay_signature);
    console.log('amount:', paymentData.amount);
    console.log('Full paymentData:', paymentData);

    const requestData = {
      orderId: paymentData.razorpay_order_id,
      paymentId: paymentData.razorpay_payment_id, 
      signature: paymentData.razorpay_signature,
      amount: paymentData.amount
    };

    console.log('Sending to backend:', requestData);

    const response = await API.post('/payment/verify', requestData);
    return response.data;
  },

  getKey: async () => {
    const response = await API.get('/payment/key')
    return response.data
  }
}