import React, { useContext, useEffect } from 'react'
import { MainContext } from '../components/MainContext';

type Props = {}

const HomeScreen = (props: Props) => {
    const { token } = useContext(MainContext);

    useEffect(() => {
      if (!token) {
        window.location.href = '/login';
      }
    
      
    }, [token])
    

  return (
    <div>HomeScreen</div>
  )
}

export default HomeScreen