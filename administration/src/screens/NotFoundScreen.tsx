import React from "react";
import { Link } from "react-router-dom";
import { Card, Empty } from "../ui/kit";

const NotFoundScreen = () => (
  <Card>
    <Empty
      icon="alert"
      title="This page doesn't exist"
      action={
        <Link to="/" className="text-sm font-semibold text-accent-ink hover:underline">
          Back to the dashboard
        </Link>
      }
    />
  </Card>
);

export default NotFoundScreen;
