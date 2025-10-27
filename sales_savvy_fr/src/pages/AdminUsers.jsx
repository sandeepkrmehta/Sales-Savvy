import { useState, useEffect } from 'react'
import { useAuth } from '../context/AuthContext'
import { userService } from '../services/user'
import Loading from '../components/common/Loading'

const AdminUsers = () => {
  const { user } = useAuth()
  const [users, setUsers] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    if (user && user.role === 'ROLE_ADMIN') {
      loadUsers()
    }
  }, [user])

  const loadUsers = async () => {
    try {
      const data = await userService.getAllUsers()
      setUsers(data)
    } catch (err) {
      setError('Failed to load users')
      console.error('Error loading users:', err)
    } finally {
      setLoading(false)
    }
  }

  const handleDeleteUser = async (userId, username) => {
    if (!window.confirm(`Are you sure you want to delete user: ${username}?`)) return
    try {
      await userService.deleteUser(userId)
      alert('User deleted successfully')
      loadUsers()
    } catch (error) {
      alert('Failed to delete user')
      console.error('Error deleting user:', error)
    }
  }

  if (!user || user.role !== 'ROLE_ADMIN') {
    return (
      <div className="text-center mt-5">
        <h2 className="text-danger">Access Denied</h2>
        <p>You need administrator privileges to access this page.</p>
      </div>
    )
  }

  if (loading) return <Loading />
  if (error) return <div className="alert alert-danger text-center mt-3">{error}</div>

  return (
    <div className="container py-4">
      {/* Header */}
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h2 className="fw-bold">User Management</h2>
          <p className="text-muted mb-0">Manage all users in the system</p>
        </div>
        <button className="btn btn-primary">
          <i className="bi bi-person-plus"></i> Add User
        </button>
      </div>

      {/* Table */}
      <div className="table-responsive shadow-sm rounded">
        <table className="table table-hover align-middle">
          <thead className="table-dark">
            <tr>
              <th scope="col">#</th>
              <th scope="col">Username</th>
              <th scope="col">Email</th>
              <th scope="col">Role</th>
              <th scope="col">Gender</th>
              <th scope="col">DOB</th>
              <th scope="col" className="text-center">Actions</th>
            </tr>
          </thead>
          <tbody>
            {users.map(u => (
              <tr key={u.id}>
                <td>{u.id}</td>
                <td>{u.username}</td>
                <td>{u.email}</td>
                <td>
                  <span
                    className={`badge ${
                      u.role === 'ROLE_ADMIN'
                        ? 'bg-danger'
                        : 'bg-success'
                    }`}
                  >
                    {u.role?.replace('ROLE_', '')}
                  </span>
                </td>
                <td>{u.gender || '-'}</td>
                <td>{u.dob || '-'}</td>
                <td className="text-center">
                  <div className="btn-group">
                    <button
                      className="btn btn-sm btn-outline-primary"
                      onClick={() => {/* TODO: Add Edit User */}}
                    >
                      <i className="bi bi-pencil-square"></i> Edit
                    </button>
                    <button
                      className="btn btn-sm btn-outline-danger"
                      onClick={() => handleDeleteUser(u.id, u.username)}
                      disabled={u.username === 'admin'}
                    >
                      <i className="bi bi-trash"></i> Delete
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        {users.length === 0 && (
          <div className="text-center text-muted py-3">
            <p>No users found.</p>
          </div>
        )}
      </div>

      {/* Stats Section */}
      <div className="row mt-5">
        <div className="col-md-4 mb-3">
          <div className="card text-center shadow-sm border-0">
            <div className="card-body">
              <h5 className="card-title text-secondary">Total Users</h5>
              <h2 className="fw-bold text-dark">{users.length}</h2>
            </div>
          </div>
        </div>
        <div className="col-md-4 mb-3">
          <div className="card text-center shadow-sm border-0">
            <div className="card-body">
              <h5 className="card-title text-danger">Admins</h5>
              <h2 className="fw-bold">
                {users.filter(u => u.role === 'ROLE_ADMIN').length}
              </h2>
            </div>
          </div>
        </div>
        <div className="col-md-4 mb-3">
          <div className="card text-center shadow-sm border-0">
            <div className="card-body">
              <h5 className="card-title text-success">Customers</h5>
              <h2 className="fw-bold">
                {users.filter(u => u.role === 'ROLE_CUSTOMER').length}
              </h2>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default AdminUsers;