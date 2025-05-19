import React, { useContext, useEffect } from 'react'
import { CommunityEventRepository } from '../repositories/CommunityEventRepository'
import { MainContext } from '../components/MainContext'

type Props = {}

const CommunityScreen = (props: Props) => {
  const { token } = useContext(MainContext)

  useEffect(() => {
    async function getEvents() {
      const events = await CommunityEventRepository.getAllEvents(token);
      console.log(events)
    }
    getEvents();


  }, [])

  return (
    <div>CommunityScreen</div>
  )
}

export default CommunityScreen