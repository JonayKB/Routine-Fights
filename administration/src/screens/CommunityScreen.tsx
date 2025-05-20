import React, { FormEvent, useContext, useEffect, useState } from 'react'
import { CommunityEventRepository } from '../repositories/CommunityEventRepository'
import { MainContext } from '../components/MainContext'
import CommunityEvent from '../models/CommunityEvent'
import CommunityEventComponent from '../components/CommunityEventComponent'
import styles from '../styles/Styles'
import Activity from '../models/Activity'
import { ImageRepository } from '../repositories/ImageRepository'

type Props = {}

const CommunityScreen = (props: Props) => {
  const { token } = useContext(MainContext)
  const [events, setEvents] = useState<Array<CommunityEvent>>()
  const [activities, setActivities] = useState<Array<Activity>>()


  useEffect(() => {
    if (!token) {
      window.location.href = "/login";
    }
    async function getEvents() {
      try {
        const eventsData = await CommunityEventRepository.getAllEvents(token);
        const events = eventsData.map((event: any) => ({
          ...event,
          startDate: new Date(event.startDate),
          finishDate: new Date(event.finishDate),
        })) as CommunityEvent[];
        setEvents(events);

      } catch (error) {
        console.error("Error fetching events:", error);
      }
    }
    getEvents();
  }, [])
  async function onSubmitEvent(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    const name = formData.get("name") as string;
    const startDate = formData.get("startDate") as string;
    const finishDate = formData.get("finishDate") as string;
    const totalRequired = parseInt(formData.get("totalRequired") as string);
    const image = formData.get("image") as File;
    const activities = formData.getAll("activities") as string[];
    const imageName = await ImageRepository.uploadImage(
      token,
      image
    )

    CommunityEventRepository.createEvent(
      token,
      {
        id: "",
        name,
        startDate: new Date(startDate),
        finishDate: new Date(finishDate),
        totalRequired,
        image: imageName,
      },
      activities
    )
      .then(() => {
        alert("Event created successfully");
        window.location.reload();
      })
      .catch((error) => {
        console.error("Error creating event:", error);
        alert("Error creating event");
      });
  }

  return (
    <div style={styles.page}>
      <div style={{ ...styles.container, padding: 20 }}>
        {events && events.length > 0 ? (
          events.map((event) => (
            <CommunityEventComponent
              key={event.id}
              communityEvent={event}
            />
          ))
        ) : (
          <p>No events available</p>
        )}
        <div style={{ ...styles.container, padding: 20, backgroundColor: "#f9f9f9", borderRadius: 8 }}>
          <h2 style={{ ...styles.header, marginBottom: 16 }}>Create Community Event</h2>
          <form onSubmit={onSubmitEvent}>
            <div style={styles.formGroup}>
              <label htmlFor="name" style={styles.label}>Name</label>
              <input
                id="name"
                type="text"
                name="name"
                placeholder="Enter event name"
                style={styles.input}
                required
              />
            </div>
            <div style={styles.formGroup}>
              <label htmlFor="startDate" style={styles.label}>Start Date</label>
              <input
                id="startDate"
                type="date"
                name="startDate"
                style={styles.input}
                required
              />
            </div>
            <div style={styles.formGroup}>
              <label htmlFor="finishDate" style={styles.label}>Finish Date</label>
              <input
                id="finishDate"
                type="date"
                name="finishDate"
                style={styles.input}
                required
              />
            </div>
            <div style={styles.formGroup}>
              <label htmlFor="totalRequired" style={styles.label}>Total Required</label>
              <input
                id="totalRequired"
                type="number"
                name="totalRequired"
                placeholder="Enter total required"
                style={styles.input}
                required
              />
            </div>
            <div style={styles.formGroup}>
              <label htmlFor="image" style={styles.label}>Image</label>
              <input
                id="image"
                type="file"
                name="image"
                accept="image/*"
                style={styles.input}
                required
              />
            </div>
            <div style={styles.formGroup}>
              <label htmlFor="activities" style={styles.label}>Activities</label>
              <select id="activities" name="activities" multiple style={styles.input}>
                {activities?.map((activity) => (
                  <option key={activity.id} value={activity.id}>
                    {activity.name}
                  </option>
                ))}
              </select>
            </div>
            <button type="submit" style={styles.button}>Create Event</button>
          </form>
        </div>
      </div>

    </div>
  )
}

export default CommunityScreen