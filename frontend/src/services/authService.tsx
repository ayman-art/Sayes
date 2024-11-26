export const loginUser = async (name: string, password: string) => {
    const response = await fetch('/api/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ name, password }),
    });
  
    const data = await response.json();
    if (response.ok && data.token) {
      // Save JWT to localStorage
      localStorage.setItem('jwtToken', data.token);
    } else {
      throw new Error('Login failed');
    }
    return data;
  };
  
  export const registerDriver = async (
    name: string,
    password: string,
    role: string,
    plateNumber: string,
    licenseNumber: string
  ) => {
    const response = await fetch('/api/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ name, password, role, plateNumber, licenseNumber }),
    });
  
    const data = await response.json();
    if (response.ok && data.token) {
      // Save JWT to localStorage
      localStorage.setItem('jwtToken', data.token);
    } else {
      throw new Error('Registration failed');
    }
    return data;
  };

  export const registerLotManager = async (name: string, password: string, role: string) => {
    const response = await fetch('/api/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ name, password, role }),
    });
  
    const data = await response.json();
    if (response.ok && data.token) {
      // Save JWT to localStorage
      localStorage.setItem('jwtToken', data.token);
    } else {
      throw new Error('Registration failed');
    }
    return data;
  };
  
  // Utility to check if the user is authenticated by validating the JWT
  export const getToken = (): string | null => {
    return localStorage.getItem('jwtToken');
  };
  
  export const logoutUser = () => {
    // Remove the JWT from localStorage
    localStorage.removeItem('jwtToken');
  };
  